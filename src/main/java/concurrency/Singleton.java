package concurrency;

/**
 * Double-checked locking (DCL) is an optimization used to minimize the cost of synchronization. -
 * The idea here is to avoid locking every time getInstance() is called. - Instead use the volatile
 * keyword on the resource to make sure other threads see the init immediately - The second instance
 * null check is in the synchronized block so that makes sure the init happens right
 */
class Singleton {
  private static volatile Singleton instance;

  private Singleton() {}

  public static Singleton getInstance() {
    if (instance == null) {
      synchronized (Singleton.class) {
        if (instance == null) {
          instance = new Singleton();
        }
      }
    }
    return instance;
  }
}
