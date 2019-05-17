package hu.lsm.concurrency.practice;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class CyclicBarrierExampleTest {

    CyclicBarrierExample cyclicBarrierExample = new CyclicBarrierExample();

    @Test
    public void testCyclicBarrierThreadsWillWaitForEachOther() throws InterruptedException {
        //creating CyclicBarrier with 3 parties i.e. 3 Threads needs to call await()
        final CyclicBarrier cb = new CyclicBarrier(3, () -> System.out.println("All parties are arrived at barrier, lets play"));

        AtomicInteger counter = new AtomicInteger(0);
        System.out.println("Counter: " + counter.intValue());
        List<Thread> threads = new ArrayList<>();
        // Run 10 threads to increment the same variable
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(new CyclicBarrierExample.Task(cb, counter), "Thread #" + i);
            thread.start();
            threads.add(thread);
        }
        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println("Counter: " + counter.intValue());
        assertEquals(3, counter.intValue());
    }

    @Test
    public void testCyclicBarrierThreadsJust2OfUs() throws InterruptedException {
        //creating CyclicBarrier with 3 parties i.e. 3 Threads needs to call await()
        final CyclicBarrier cb = new CyclicBarrier(3, () -> System.out.println("All parties are arrived at barrier, lets play"));

        AtomicInteger counter = new AtomicInteger(0);
        List<Thread> threads = new ArrayList<>();
        // Run 10 threads to increment the same variable
        for (int i = 1; i <= 2; i++) {
            Thread thread = new Thread(new CyclicBarrierExample.Task(cb, counter), "Thread #" + i);
            thread.start();
            threads.add(thread);
        }
        Thread.sleep(300);
        System.out.println("All two threads are still waiting on barrier, but will never step forward");
        assertEquals(3, cb.getParties());
        assertEquals(2, cb.getNumberWaiting());

        // Counter was never changed
        assertEquals(0, counter.intValue());
        // cleaning
        for(Thread thread: threads){
            thread.interrupt();
        }
    }

    @Test
    public void testCyclicBarrierReset() throws InterruptedException {
        //creating CyclicBarrier with 3 parties i.e. 3 Threads needs to call await()
        final CyclicBarrier cb = new CyclicBarrier(3, () -> System.out.println("All parties are arrived at barrier, lets play"));

        AtomicInteger counter = new AtomicInteger(0);
        List<Thread> threads = new ArrayList<>();
        // Run 10 threads to increment the same variable
        for (int i = 1; i <= 2; i++) {
            Thread thread = new Thread(new CyclicBarrierExample.Task(cb, counter), "Thread #" + i);
            thread.start();
            threads.add(thread);
        }
        Thread.sleep(300);
        assertEquals(3, cb.getParties());
        assertEquals(2, cb.getNumberWaiting());
        cb.reset();
        assertEquals(3, cb.getParties());
        assertEquals(0, cb.getNumberWaiting());
        for (int i = 3; i <= 5; i++) {
            Thread thread = new Thread(new CyclicBarrierExample.Task(cb, counter), "Thread #" + i);
            thread.start();
            threads.add(thread);
        }
        Thread.sleep(300);
        System.out.println("Thread #1 and Thread #2 will drop broken barrier exception");
        assertEquals(3, counter.intValue());

    }

}