package hu.lsm.concurrency.practice;

import java.security.InvalidParameterException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureExample {

    private ExecutorService executor;

    public static void main(String args[]) {
        FutureExample futureExample = new FutureExample(1);
        Future<Long> square = futureExample.calculateSquare(10);
        try {
            // Blocking the main thread
            System.out.println(square.get());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public FutureExample(int threadNumber) {
        if (threadNumber < 1) {
            throw new InvalidParameterException("Invalid thread number: " + threadNumber);
        }
        if (threadNumber == 1) {
            executor = Executors.newSingleThreadExecutor();
        } else {
            executor = Executors.newFixedThreadPool(threadNumber);
        }
    }

    public Future<Long> calculateSquare(Integer input) {
        return executor.submit(() -> {
            Thread.sleep(1000);
            return input * input * 1l;
        });
    }

    public Future<Long> calculateSquareJava7(Integer input) {
        Callable<Long> task = new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                Thread.sleep(1000);
                return input * input * 1l;
            }
        };
        return executor.submit(task);
    }
}
