package hu.lsm.concureny.practice;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DeadLockTest {

    @Test
    public void testDeadLock() throws InterruptedException {
        DeadLock.BadThreadDemo1 bad1 = new DeadLock.BadThreadDemo1();
        DeadLock.BadThreadDemo2 bad2 = new DeadLock.BadThreadDemo2();
        bad1.start();
        bad2.start();
        Thread.sleep(5000);
        assertEquals(bad1.getState(), Thread.State.BLOCKED);
        assertEquals(bad2.getState(), Thread.State.BLOCKED);
        bad1.interrupt();
        bad2.interrupt();
    }

    @Test
    public void testDeadLockSolution() throws InterruptedException {
        DeadLock.GoodThreadDemo1 good1 = new DeadLock.GoodThreadDemo1();
        DeadLock.GoodThreadDemo2 good2 = new DeadLock.GoodThreadDemo2();
        good1.start();
        good2.start();
        Thread.sleep(5000);
        assertEquals(good1.getState(), Thread.State.TERMINATED);
        assertEquals(good2.getState(), Thread.State.TERMINATED);
    }
}
