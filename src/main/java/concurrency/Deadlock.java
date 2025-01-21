package concurrency;

/**
 * Potential deadlock situation when two threads tom, jerry take each other key and ask for their
 * own - Can use the cmd line util jcmd to detect deadlock via the thread dump - Get pid: ps -a -
 * Get thread dump: jcmd <pid> Thread.print - Non-trival to do this on a remote prod scenario - Can
 * use the cmd line util jstack as well - Get pid: ps -a - Get thread dump: jstack <pid> - GUI tools
 * available as well - JConsole, VisualVM, etc. - To resolve, mostly preemptive techniques - Avoid
 * nested locks if possible - Acquire with timeout - Lock ordering (below both take tom's key first
 * and then both ask for jerry's key) - Use ReentrantLock with tryLock() with timeout
 */
public class Deadlock {

  public static void main(String[] args) {

    Object tomKey = new Object();
    Object jerryKey = new Object();

    Thread jerry =
        new Thread(
            () -> {
              synchronized (tomKey) {
                System.out.println("Jerry has acquired Tom's Key");
                try {
                  System.out.println("Jerry doing some work...");
                  Thread.sleep(1000);
                  System.out.println("Jerry done with work");
                } catch (InterruptedException e) {
                  throw new RuntimeException(e);
                }
                synchronized (jerryKey) {
                  System.out.println("Jerry has got his key");
                }
              }
            },
            "jerry");

    Thread tom =
        new Thread(
            () -> {
              synchronized (jerryKey) {
                System.out.println("Tom has acquired Jerry's Key");
                try {
                  System.out.println("Tom doing some work...");
                  Thread.sleep(1000);
                  System.out.println("Tom done with work");
                } catch (InterruptedException e) {
                  throw new RuntimeException(e);
                }
                synchronized (tomKey) {
                  System.out.println("Tom has got his key");
                }
              }
            },
            "tom");

    jerry.start();
    tom.start();
  }
}
