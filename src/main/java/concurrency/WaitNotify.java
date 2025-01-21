package concurrency;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Used to coordinate between two threads using a monitor lock (intrinsic lock or custom defined obj
 * lock) - wait() will automatically release the lock - notify() will notify one thread. If many
 * waiting, its random - lock release will happen after synchronized block complete, not immediately
 * when this is called - notifyAll() will notify all waiting threads - Prefer 'while' loops instead
 * of 'if' for the condition check - Threads may wake up from wait() without notify() due to JVM
 * optimizations. Alternatively use the Reentrant lock with Conditions to coordinate between threads
 * - More flexible than the synchronized block with better interface support - await() -> similar to
 * wait() - signal() -> similar to notify() - signalAll() -> similar to notifyAll() ------- Example:
 * Print numbers and letters strictly alternating using two threads - A1B2C3D4E5...
 */
public class WaitNotify {

  // for wait/notify
  private final Object monitor = new Object();
  private boolean printLetter = true;

  // for await/signal
  private final Lock lock = new ReentrantLock();
  private final Condition letterTurn = lock.newCondition();
  private final Condition numberTurn = lock.newCondition();

  public void printLetterUsingMonitor(char c) {
    synchronized (monitor) {
      try {
        while (!printLetter) monitor.wait();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      System.out.print(c + ", ");
      printLetter = false;
      monitor.notify();
    }
  }

  public void printNumberUsingMonitor(int n) {
    synchronized (monitor) {
      try {
        while (printLetter) monitor.wait();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      System.out.print(n + ", ");
      printLetter = true;
      monitor.notify();
    }
  }

  public void printLetterUsingLock(char c) {
    lock.lock();
    try {
      while (!printLetter) letterTurn.await();
      System.out.print(c + ", ");
      printLetter = false;
      numberTurn.signal();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } finally {
      lock.unlock();
    }
  }

  public void printNumberUsingLock(int n) {
    lock.lock();
    try {
      while (printLetter) numberTurn.await();
      System.out.print(n + ", ");
      printLetter = true;
      letterTurn.signal();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } finally {
      lock.unlock();
    }
  }

  public static void main(String[] args) {

    WaitNotify wn = new WaitNotify();

    Runnable letterTask =
        () -> {
          for (char c = 'A'; c <= 'Z'; c++) {
            wn.printLetterUsingLock(c);
          }
        };
    Runnable numberTask =
        () -> {
          for (int n = 1; n <= 26; n++) {
            wn.printNumberUsingLock(n);
          }
        };

    Thread letterThread = new Thread(letterTask, "letter-thread");
    Thread numberThread = new Thread(numberTask, "number-thread");

    letterThread.start();
    numberThread.start();

    try {
      letterThread.join();
      numberThread.join();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
