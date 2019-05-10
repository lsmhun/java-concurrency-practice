package hu.lsm.concureny.practice;

/**
 * Classic deadlock example
 */
public class DeadLock {

    public static Object LOCK_1 = new Object();
    public static Object LOCK_2 = new Object();

    public static void main(String args[]) {
        BadThreadDemo1 t1 = new BadThreadDemo1();
        BadThreadDemo2 t2 = new BadThreadDemo2();
        t1.start();
        t2.start();
    }


    public static class BadThreadDemo1 extends Thread {
        public void run() {
            synchronized (LOCK_1) {
                System.out.println("Thread 1: Holding lock 1...");

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Thread 1: Waiting for lock 2...");

                synchronized (LOCK_2) {
                    System.out.println("Thread 1: Holding lock 1 & 2...");
                }
            }
        }
    }

    public static class BadThreadDemo2 extends Thread {
        public void run() {
            synchronized (LOCK_2) {
                System.out.println("Thread 2: Holding lock 2...");

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
                System.out.println("Thread 2: Waiting for lock 1...");

                synchronized (LOCK_1) {
                    System.out.println("Thread 2: Holding lock 1 & 2...");
                }
            }
        }
    }

    /**
     * Just changed the LOCK order and it can solve the basic issue
     */
    public static class GoodThreadDemo1 extends Thread {
        public void run() {
            synchronized (LOCK_1) {
                System.out.println("Thread 1: Holding lock 1...");

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Thread 1: Waiting for lock 2...");

                synchronized (LOCK_2) {
                    System.out.println("Thread 1: Holding lock 1 & 2...");
                }
            }
        }
    }

    public static class GoodThreadDemo2 extends Thread {
        public void run() {
            synchronized (LOCK_1) {
                System.out.println("Thread 2: Holding lock 1...");

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Thread 2: Waiting for lock 2...");

                synchronized (LOCK_2) {
                    System.out.println("Thread 2: Holding lock 1 & 2...");
                }
            }
        }
    }

}
