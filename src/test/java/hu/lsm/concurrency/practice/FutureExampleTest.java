package hu.lsm.concurrency.practice;

import org.junit.Test;

import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;

public class FutureExampleTest {

    @Test
    public void callFutureTest() throws InterruptedException {
        FutureExample futureExample = new FutureExample(1);
        Future<Long> future = futureExample.calculateSquare(10);

        int checkingCounter = 0;
        while(!future.isDone()) {
            checkingCounter++;
            System.out.println("#" + checkingCounter + " Calculating...");
            Thread.sleep(400);
        }
        assertEquals(3, checkingCounter);
    }

    @Test
    public void callFutureJava7StyleTest() throws InterruptedException {
        FutureExample futureExample = new FutureExample(1);
        Future<Long> future = futureExample.calculateSquareJava7(10);

        int checkingCounter = 0;
        while(!future.isDone()) {
            checkingCounter++;
            System.out.println("#" + checkingCounter + " Calculating...");
            Thread.sleep(400);
        }
        assertEquals(3, checkingCounter);
    }

    @Test
    public void checkFutureWithOneThread() throws InterruptedException  {
        // Checking with 1 executor
        checkFutureWithOneOrMoreThreads(1, 5);
    }

    @Test
    public void checkFutureParallel() throws InterruptedException  {
        // Checking with 2 executors
        checkFutureWithOneOrMoreThreads(2, 3);
    }

    private void checkFutureWithOneOrMoreThreads(int threadNumber, int expectedWaitingCycle) throws InterruptedException{
        FutureExample futureSingleThreaded = new FutureExample(threadNumber);
        Future<Long> future1 = futureSingleThreaded.calculateSquare(10);
        Future<Long> future2 = futureSingleThreaded.calculateSquare(100);

        int checkingCounter = 0;
        while(!(future1.isDone() && future2.isDone())) {
            checkingCounter++;
            System.out.println("#" + checkingCounter + " Calculating...");
            Thread.sleep(450);
        }
        assertEquals(expectedWaitingCycle, checkingCounter);
    }
}
