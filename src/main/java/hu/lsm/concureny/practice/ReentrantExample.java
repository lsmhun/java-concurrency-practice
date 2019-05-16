package hu.lsm.concureny.practice;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantExample {

    private static final int THREAD_NUMBER = 8;

    interface ParallelMapEx {
        void addValue(String key, Integer value);

        Integer getValue(String key);

        int getSize();
    }


    /**
     * Using ReentrantLock
     */
    public static class ReentrantEx implements ParallelMapEx {
        private ReentrantLock reentrantLock = new ReentrantLock();
        private Map<String, Integer> insideMap = new HashMap<>();

        @Override
        public void addValue(String key, Integer value) {
            try {
                while (reentrantLock.isLocked()) {
                    System.out.println("Thread #" + Thread.currentThread().getName() + " is waiting for Lock regarding WRITE");
                }
                reentrantLock.lock();
                insideMap.put(key, value);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
        }

        public Integer getValue(String key) {
            try {
                reentrantLock.lock();
                return insideMap.get(key);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                reentrantLock.unlock();
            }
            return null;
        }

        public int getSize() {
            try {
                reentrantLock.lock();
                return insideMap.size();
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    /**
     * Read Write Lock example
     */
    public static class ReadWriteEx implements ParallelMapEx {
        private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        private Map<String, Integer> insideMap = new HashMap<>();

        public void addValue(String key, Integer value) {
            try {
                while (reentrantReadWriteLock.isWriteLocked()) {
                    System.out.println("Thread #" + Thread.currentThread().getName() + " is waiting for Write Lock");
                    //Thread.sleep(10);
                }
                reentrantReadWriteLock.writeLock().lock();
                insideMap.put(key, value);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                reentrantReadWriteLock.writeLock().unlock();
            }
        }

        public Integer getValue(String key) {
            try {
                reentrantReadWriteLock.readLock().lock();
                return insideMap.get(key);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                reentrantReadWriteLock.readLock().unlock();
            }
            return null;
        }

        public int getSize() {
            try {
                reentrantReadWriteLock.readLock().lock();
                return insideMap.size();
            } finally {
                reentrantReadWriteLock.readLock().unlock();
            }
        }
    }

    public static class ConcurrentHashMapExample implements ParallelMapEx {
        private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        private Map<String, Integer> insideMap = new ConcurrentHashMap<>();

        public void addValue(String key, Integer value) {
            insideMap.put(key, value);
        }

        public Integer getValue(String key) {
            return insideMap.get(key);
        }

        public int getSize() {
            return insideMap.size();
        }
    }


    public static void main(String[] args) {
        //ReentrantExample.ParallelMapEx reentrantEx = new ReadWriteEx();
        //ReentrantExample.ParallelMapEx reentrantEx = new ReentrantEx();
        ReentrantExample.ParallelMapEx reentrantEx = new ConcurrentHashMapExample();
        for (int t = 1; t <= THREAD_NUMBER; t++) {
            Thread t1 = new Thread(String.valueOf(t)) {
                @Override
                public void run() {
                    int tn = Integer.valueOf(Thread.currentThread().getName());
                    int value = tn * 1000;
                    for (int i = 1; i <= 10; i++) {
                        try {
                            // just for more visible behavior
                            Thread.currentThread().sleep(30);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        value++;
                        reentrantEx.addValue(String.valueOf(value), value);
                    }

                }
            };
            t1.start();
        }

        int counter = 0;
        while (reentrantEx.getSize() != THREAD_NUMBER * 10) {
            counter++;
        }
        System.out.println("ReadCounter:" + counter + " HashMap size: " + reentrantEx.getSize());
    }
}
