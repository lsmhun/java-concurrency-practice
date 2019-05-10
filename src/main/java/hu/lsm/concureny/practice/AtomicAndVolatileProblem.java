package hu.lsm.concureny.practice;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * https://stackoverflow.com/questions/17748078/simplest-and-understandable-example-of-volatile-keyword-in-java
 * <p>
 * Volatile --> Guarantees visibility and NOT atomicity
 * <p>
 * Synchronization (Locking) --> Guarantees visibility and atomicity (if done properly)
 * <p>
 * Volatile is not a substitute for synchronization
 */
public class AtomicAndVolatileProblem {

    private int intCounter = 0;
    private volatile int intCounterVol = 0;

    private AtomicInteger atomIntCounter = new AtomicInteger(0);

    public void incIntNotVolatile() {
        intCounter++;
    }

    public void incIntVolatile() {
        intCounterVol++;
    }

    public int getIntNotVolatile() {
        return intCounter;
    }

    public int getIntVolatile() {
        return intCounterVol;
    }

    public synchronized void syncIncIntVolatile() {
        intCounter++;
    }

    public void incrementAtomicIntegerCounter(){
        atomIntCounter.incrementAndGet();
    }

    public int getAtomIntCounter(){
        return atomIntCounter.intValue();
    }

}
