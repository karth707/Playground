package concurrency;

/**
 * Two types:
 *   Read-Modify-Write
 *     - shared resource: c = 5
 *     - 1 - A: read 5, mod 6
 *     - 2 - B: read 5, mod 6
 *     - 3 - A: write 6
 *     - 4 - B: write 6
 *     - shared resource should be 7 and not 6
 *     - Interleived operations
 *   Check-then-Act
 *     - Critical section is running via a condition check
 *     - Only one thread should check the condition at once
 *     - If A, B both do the same check
 *     - Both see true, where it should be only for one
 *     - This leads to both modifying the critical section
 * Order of execution is non-deterministic, so we need to make things thread safe
 */
public class RaceConditions {

    private static class CheckThenActRunnable implements Runnable {
        private int balance = 100;

        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            System.out.println(name + " is checking to reduce balance");

            // protect critical section
            synchronized (this) {
                if (balance >= 100) {
                    System.out.println(name + " is reducing balance");
                    balance -= 50;
                } else {
                    System.out.println(name + " could not reduce balance");
                }
            }

            System.out.println(name + " seeing current balance: " + balance);
        }
    }

    private static void testCheckThenAct() throws InterruptedException {
        CheckThenActRunnable runnable = new CheckThenActRunnable();
        Thread t1 = new Thread(runnable, "T1");
        Thread t2 = new Thread(runnable, "T2");
        t1.start();
        t2.start();
    }

    private static class ReadModifyWriteRunnable implements Runnable {
        private int shared = 0;

        @Override
        public void run() {
            // protect critical section
            synchronized (this) {
                for (int i=0; i<10000; i++) {
                    shared++;
                }
            }
        }

        public int getShared() {
            return shared;
        }
    }

    private static void testReadModifyWrite() throws InterruptedException {
        ReadModifyWriteRunnable runnable = new ReadModifyWriteRunnable();
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Shared counter = " + runnable.getShared());
    }

    public static void main(String[] args) throws InterruptedException {
        testReadModifyWrite();
        testCheckThenAct();
    }
}
