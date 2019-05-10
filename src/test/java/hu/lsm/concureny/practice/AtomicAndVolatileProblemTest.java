package hu.lsm.concureny.practice;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AtomicAndVolatileProblemTest {

    private static final int TEST_THREAD_COUNT = 10;
    private static final int TEST_INCREMENT_NUMBER = 10_000;

    AtomicAndVolatileProblem problematicExample = new AtomicAndVolatileProblem();

    @Test
    public void testNotVolatileIntCounterProblem() throws InterruptedException {
        long prevTime = System.currentTimeMillis();
        List<Thread> threads = new ArrayList<>();
        // Run 10 threads to increment the same variable
        for (int i = 0; i < TEST_THREAD_COUNT; i++) {
            Thread thread = new IncrementerNonVolatileIntCounter();
            thread.start();
            threads.add(thread);
        }
        for (Thread thread : threads) {
            thread.join();
        }

        long afterTime = System.currentTimeMillis();
        System.out.println("Running time: " + (afterTime - prevTime) +
                " Result value: " + problematicExample.getIntNotVolatile());
        assertNotEquals(TEST_THREAD_COUNT * TEST_INCREMENT_NUMBER, problematicExample.getIntNotVolatile());
    }

    /**
     * Volatile is not enough:
     * //i++
     * $tmp = i; // Store value to a temporary storage
     * $tmp = $tmp + 1; // Increment value in the temporary storage
     * i = $tmp; // Write result value back
     *
     * @throws InterruptedException
     */
    @Test
    public void testVolatileIntCounterProblem() throws InterruptedException {
        long prevTime = System.currentTimeMillis();
        List<Thread> threads = new ArrayList<>();
        // Run 10 threads to increment the same variable
        for (int i = 0; i < TEST_THREAD_COUNT; i++) {
            Thread thread = new IncrementerVolatileIntCounter();
            thread.start();
            threads.add(thread);
        }
        for (Thread thread : threads) {
            thread.join();
        }
        long afterTime = System.currentTimeMillis();
        System.out.println("Running time: " + (afterTime - prevTime) +
                " Result value: " + problematicExample.getIntVolatile());
        assertNotEquals(TEST_THREAD_COUNT * TEST_INCREMENT_NUMBER, problematicExample.getIntVolatile());
    }

    @Test
    public void testSyncIntCounterSlow() throws InterruptedException {
        long prevTime = System.currentTimeMillis();
        List<Thread> threads = new ArrayList<>();
        // Run 10 threads to increment the same variable
        for (int i = 0; i < TEST_THREAD_COUNT; i++) {
            Thread thread = new IncrementerSyncIntCounter();
            thread.start();
            threads.add(thread);
        }
        for (Thread thread : threads) {
            thread.join();
        }

        long afterTime = System.currentTimeMillis();
        System.out.println("Running time: " + (afterTime - prevTime) +
                " Result value: " + problematicExample.getIntNotVolatile());
        assertEquals(TEST_THREAD_COUNT * TEST_INCREMENT_NUMBER, problematicExample.getIntNotVolatile());
    }

    @Test
    public void testAtomicIntegerCounter() throws InterruptedException {
        long prevTime = System.currentTimeMillis();
        List<Thread> threads = new ArrayList<>();
        // Run 10 threads to increment the same variable
        for (int i = 0; i < TEST_THREAD_COUNT; i++) {
            Thread thread = new IncrementerAtomicIntegerCounter();
            thread.start();
            threads.add(thread);
        }
        for (Thread thread : threads) {
            thread.join();
        }

        long afterTime = System.currentTimeMillis();
        System.out.println("Running time: " + (afterTime - prevTime) +
                " Result value: " + problematicExample.getAtomIntCounter());
        assertEquals(TEST_THREAD_COUNT * TEST_INCREMENT_NUMBER, problematicExample.getAtomIntCounter());
    }

    private class IncrementerNonVolatileIntCounter extends Thread {
        public void run() {
            for (int i = 0; i < TEST_INCREMENT_NUMBER; i++) {
                problematicExample.incIntNotVolatile();
            }
        }
    }

    private class IncrementerVolatileIntCounter extends Thread {
        public void run() {
            for (int i = 0; i < TEST_INCREMENT_NUMBER; i++) {
                problematicExample.incIntVolatile();
            }
        }
    }

    private class IncrementerSyncIntCounter extends Thread {
        public void run() {
            for (int i = 0; i < TEST_INCREMENT_NUMBER; i++) {
                problematicExample.syncIncIntVolatile();
            }
        }
    }

    private class IncrementerAtomicIntegerCounter extends Thread {
        public void run() {
            for (int i = 0; i < TEST_INCREMENT_NUMBER; i++) {
                problematicExample.incrementAtomicIntegerCounter();
            }
        }
    }
}
