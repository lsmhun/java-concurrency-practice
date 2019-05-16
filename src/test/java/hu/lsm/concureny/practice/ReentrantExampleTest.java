package hu.lsm.concureny.practice;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReentrantExampleTest {

    private static final int THREAD_NUMBER = 8;
    private static final int WRITE_NUMBER = 10;

    @Test
    public void testReentrantLock() {
        long before = System.currentTimeMillis();
        ReentrantExample.ParallelMapEx example = new ReentrantExample.ReentrantEx();
        initTestingThreads(example);

        int counter = 0;
        while (example.getSize() != THREAD_NUMBER * WRITE_NUMBER) {
            counter++;
        }
        System.out.println("ReadCounter:" + counter + " HashMap size: " + example.getSize());
        long after = System.currentTimeMillis();
        System.out.println("Time measure: " + (after - before));
        assertEquals(THREAD_NUMBER * WRITE_NUMBER, example.getSize());
        String lastKey = String.valueOf(THREAD_NUMBER * 1000 + WRITE_NUMBER);
        assertEquals(Integer.valueOf(lastKey), example.getValue(lastKey));
    }

    @Test
    public void testReentrantReadWriteLock() {
        long before = System.currentTimeMillis();
        ReentrantExample.ParallelMapEx example = new ReentrantExample.ReadWriteEx();
        initTestingThreads(example);

        int counter = 0;
        while (example.getSize() != THREAD_NUMBER * WRITE_NUMBER) {
            counter++;
        }
        System.out.println("ReadCounter:" + counter + " HashMap size: " + example.getSize());
        long after = System.currentTimeMillis();
        System.out.println("Time measure: " + (after - before));
        assertEquals(THREAD_NUMBER * WRITE_NUMBER, example.getSize());
        String lastKey = String.valueOf(THREAD_NUMBER * 1000 + WRITE_NUMBER);
        assertEquals(Integer.valueOf(lastKey), example.getValue(lastKey));
    }

    @Test
    public void testConcurrentHashMap() {
        long before = System.currentTimeMillis();
        ReentrantExample.ParallelMapEx example = new ReentrantExample.ConcurrentHashMapExample();
        initTestingThreads(example);

        int counter = 0;
        while (example.getSize() != THREAD_NUMBER * WRITE_NUMBER) {
            counter++;
        }
        System.out.println("ReadCounter:" + counter + " HashMap size: " + example.getSize());
        long after = System.currentTimeMillis();
        System.out.println("Time measure: " + (after - before));
        assertEquals(THREAD_NUMBER * WRITE_NUMBER, example.getSize());
        String lastKey = String.valueOf(THREAD_NUMBER * 1000 + WRITE_NUMBER);
        assertEquals(Integer.valueOf(lastKey), example.getValue(lastKey));
    }

    private void initTestingThreads(ReentrantExample.ParallelMapEx parallelMap) {
        for (int t = 1; t <= THREAD_NUMBER; t++) {
            Thread t1 = new Thread(String.valueOf(t)) {
                @Override
                public void run() {
                    int tn = Integer.valueOf(Thread.currentThread().getName());
                    int value = tn * 1000;
                    for (int i = 1; i <= 10; i++) {
                        try {
                            Thread.currentThread().sleep(30);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        value++;
                        parallelMap.addValue(String.valueOf(value), value);
                    }

                }
            };
            t1.start();
        }
    }
}