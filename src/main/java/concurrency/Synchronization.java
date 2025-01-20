package concurrency;

/**
 * Prevents race condition by making the critical section synchronized
 * Single thread can get in at a time
 * - Synchronization blocks makes that block synchronized
 *   - More flexible as we can use different locks to sync different crital parts
 * - Synchronization methods makes the whole method synchronized
 *   - Might be unnecessary as we might not want to sync all the work
 *   - Uses the implicit lock of the class, shared across all sync methods in the class
 *     - static method has one lock for all instances otherwise each instance has its implicit lock
 *  - Below making the methods synchronized will make the non-critical work also blocking increasing runtime
 */
public class Synchronization {

    // critical shared resource
    private double balance;

    public Synchronization(double initialBalance) {
        this.balance = initialBalance;
    }

    public void deposit(double amount) {
        doWork(2000);   // non-critical work

        synchronized (this) {
            String thread = Thread.currentThread().getName();
            System.out.println(thread + " attempting to deposit: " + amount + ", current balance: " + balance);

            double newBalance = balance + amount;
            doWork(1000);   // critical work
            balance = newBalance;

            System.out.println(thread + " seeing new balance: " + balance);
        }
    }

    public void withdraw(double amount) {
        doWork(2000);   // non-critical work

        synchronized(this) {
            String thread = Thread.currentThread().getName();
            System.out.println(thread + " attempting to withdraw: " + amount + ", current balance: " + balance);
            if (balance >= amount) {
                double newBalance = balance - amount;
                doWork(1000);
                balance = newBalance;
            }
            System.out.println(thread + " seeing new balance: " + balance);
        }
    }

    public double getBalance() {
        return balance;
    }

    // processing work simulation
    private void doWork(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        Synchronization account = new Synchronization(100.0);

        Thread deposit1 = new Thread(() -> account.deposit(40), "deposit1");
        Thread deposit2 = new Thread(() -> account.deposit(50), "deposit2");
        Thread withdraw1 = new Thread(() -> account.withdraw(30), "withdraw1");
        Thread withdraw2 = new Thread(() -> account.withdraw(50), "withdraw2");

        deposit1.start();
        deposit2.start();
        withdraw1.start();
        withdraw2.start();

        try {
            deposit1.join();
            deposit2.join();
            withdraw1.join();
            withdraw2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Balance: " + account.getBalance()); // Expected final balance - 110

        long endTime = System.currentTimeMillis();
        // ~12 sec with synchronized method and ~6 sec with the synchronized block
        System.out.println("Total execution time (sec): " + (endTime-startTime)/1000);
    }
}
