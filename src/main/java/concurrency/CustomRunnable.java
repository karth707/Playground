package concurrency;

/**
 * Implement the Runnable instead of extending the Thread class - Advantage is mainly object
 * overhead and no multiple inheritance issues
 */
public class CustomRunnable implements Runnable {

  @Override
  public void run() {
    System.out.println("Running thread: " + Thread.currentThread().getName());
  }

  public static void main(String[] args) {

    Thread customThread = new Thread(new CustomRunnable());

    Thread lambdaThread =
        new Thread(
            () -> {
              try {
                for (int i = 0; i < 10; i++) {
                  System.out.println("User thread:  " + i);
                  Thread.sleep(1000);
                }
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
            });

    Thread demonThread =
        new Thread(
            () -> {
              try {
                for (int i = 0; i < 10; i++) {
                  System.out.println("Demon thread:  " + i);
                  Thread.sleep(2000);
                }
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
            });

    customThread.start();
    lambdaThread.start();
    demonThread.setDaemon(true);
    demonThread.start();
  }
}
