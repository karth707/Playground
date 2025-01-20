package concurrency;

public class MyRunnable implements Runnable {

    @Override
    public void run() {
        System.out.println("Running thread: " + Thread.currentThread().getName());
    }

    public static void main(String[] args) {

        Thread myThread = new Thread(new MyRunnable());

        Thread lambdaThread = new Thread(() -> {
            try {
                for (int i=0; i<10; i++) {
                    System.out.println("User thread:  " + i);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread demonThread = new Thread(() -> {
            try {
                for (int i=0; i<10; i++) {
                    System.out.println("Demon thread:  " + i);
                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        myThread.start();
        lambdaThread.start();
        demonThread.setDaemon(true);
        demonThread.start();
    }
}
