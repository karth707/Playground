package concurrency;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Instead of the Synchronized methods or the synchronized blocks, easier to use the Lock interface
 *   - Has support for non-blocking attempts like tryLock(), tryLock(long, TimeUnit)
 *   - Problem is, responsibility of the dev to manage and release locks etc.
 * - ReentrantLock allows a thread to acquire the same lock multiple times.
 *   - Particularly useful when a thread needs to access a shared resource repeatedly within its execution
 *   - Tracks a “hold count” which starts at 1 when a thread first locks and increments
 *   - Once the hold count reaches zero, the lock is fully released.
 * - ReentrantReadWriteLock more flexible for reader/writer work
 *   - Read Lock – If no thread acquired the write lock or requested for it, multiple threads can acquire the read lock.
 *   - Write Lock – If no threads are reading or writing, only one thread can acquire the write lock.
 */
public class Locks {

    private int shared = 0;

    private final Lock reentrantLock = new ReentrantLock();
    private final Lock reentrantReadLock = new ReentrantReadWriteLock().readLock();
    private final Lock reentrantWriteLock = new ReentrantReadWriteLock().writeLock();

    public void incrementShared(int value) {
        reentrantWriteLock.lock();
        String thread = Thread.currentThread().getName();
        try {
            Util.doWork(1000);
            shared += value;
            System.out.println(thread + " updated shared, shared: " + shared);
        } finally {
            reentrantWriteLock.unlock();
        }
    }

    private int getShared() {
        reentrantReadLock.lock();
        String thread = Thread.currentThread().getName();
        try {
            Util.doWork(500);
            System.out.println(thread + " reading shared value, shared: " + shared);
            return shared;
        } finally {
            reentrantReadLock.unlock();
        }
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        Locks sharedInstance = new Locks();

        Runnable writerRunnable = () -> {
            for (int i=1; i<=10; i++) {
                sharedInstance.incrementShared(10);
            }
        };
        Runnable readerRunnable = () -> {
            for (int i=1; i<=10; i++) {
                sharedInstance.getShared();
            }
        };

        Thread writeThread1 = new Thread(writerRunnable, "writer-1");
        Thread writeThread2 = new Thread(writerRunnable, "writer-2");
        Thread readThread1 = new Thread(readerRunnable, "reader-1");
        Thread readThread2 = new Thread(readerRunnable, "reader-2");
        Thread readThread3 = new Thread(readerRunnable, "reader-3");
        Thread readThread4 = new Thread(readerRunnable, "reader-4");

        writeThread1.start();
        readThread1.start();
        readThread2.start();

        writeThread2.start();
        readThread3.start();
        readThread4.start();

        try {
            writeThread1.join();
            writeThread2.join();
            readThread1.join();
            readThread2.join();
            readThread3.join();
            readThread4.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Final shared value: " + sharedInstance.getShared());

        long endTime = System.currentTimeMillis();

        // If we use the same reentrantLock for both read & write then reads will wait to get the lock
        // Using the read/write lock lets the readers read when the writer is working
        // ~20 sec when using reentrantLock read/write lock
        // ~40 sec when using the same reentrantLock non-read/write lock as its basically sequential
        System.out.println("Total execution time (sec): " + (endTime-startTime)/1000);
    }
}
