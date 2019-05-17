package hu.lsm.concurrency.practice;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
*  https://www.java67.com/2012/08/10-advanced-core-java-interview.html
* Some differences
* - CyclicBarrier can be reset 
* - CountDownLatch will 
* https://javarevisited.blogspot.com/2012/07/cyclicbarrier-example-java-5-concurrency-tutorial.html
*/
public class CyclicBarrierExample {

    //Runnable task for each thread
    public static class Task extends Thread {

        private CyclicBarrier barrier;
        private AtomicInteger counter;

        public Task(CyclicBarrier barrier, AtomicInteger counter) {
            this.counter = counter;
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                int waitingMillisec = ThreadLocalRandom.current().nextInt(0, 100);
                // simulating long running task
                System.out.println(Thread.currentThread().getName() + " will sleep " + waitingMillisec + " ms");
                Thread.sleep(waitingMillisec);
                System.out.println(Thread.currentThread().getName() + " is waiting on barrier");
                barrier.await();
                System.out.println(Thread.currentThread().getName() + " has crossed the barrier");
                counter.incrementAndGet();
            } catch (InterruptedException ex) {
                Logger.getLogger(CyclicBarrierExample.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            } catch (BrokenBarrierException ex) {
                Logger.getLogger(CyclicBarrierExample.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    public static void main(String args[]) {

        //creating CyclicBarrier with 3 parties i.e. 3 Threads needs to call await()
        final CyclicBarrier cb = new CyclicBarrier(3, () -> System.out.println("All parties are arrived at barrier, lets play"));

        AtomicInteger counter = new AtomicInteger(0);
        System.out.println("Counter: " + counter.intValue());
        //starting each of thread
        Thread t1 = new Thread(new Task(cb, counter), "Thread 1");
        Thread t2 = new Thread(new Task(cb, counter), "Thread 2");
        Thread t3 = new Thread(new Task(cb, counter), "Thread 3");

        t1.start();
        t2.start();
        t3.start();

    }

}
