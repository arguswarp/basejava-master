package ru.javawebinar.basejava;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MainConcurrency {
    public static final int THREADS_NUMBER = 10000;
    private final AtomicInteger atomicCounter = new AtomicInteger();
    private static final ThreadLocal<SimpleDateFormat> threadLocal = ThreadLocal.withInitial(()-> new SimpleDateFormat("dd.MM.yyyy HH:mm:ss"));
//    private static final Object LOCK = new Object();

//    private static final Lock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println(Thread.currentThread().getName());
        Thread thread0 = new Thread() {
            @Override
            public void run() {
                System.out.println(getName() + ", " + getState());
            }
        };
        thread0.start();

        new Thread(() -> System.out.println(Thread.currentThread().getName() + ", " + Thread.currentThread().getState())).start();
        System.out.println(thread0.getState());
        MainConcurrency mainConcurrency = new MainConcurrency();
        CountDownLatch latch = new CountDownLatch(THREADS_NUMBER);
        ExecutorService executorService = Executors.newCachedThreadPool();
//        CompletionService completionService = new ExecutorCompletionService(executorService);
//        List<Thread> threads = new ArrayList<>(THREADS_NUMBER);
        for (int i = 0; i < THREADS_NUMBER; i++) {

            Future<Integer> future = executorService.submit(() ->
//            Thread thread = new Thread(() ->
            {
                for (int j = 0; j < 100; j++) {
                    mainConcurrency.inc();
                    System.out.println(threadLocal.get().format(new Date()));
                }
                latch.countDown();
                return 5;
            });

//            thread.start();
//            threads.add(thread);
        }
        latch.await();
        executorService.shutdown();
        System.out.println(mainConcurrency.atomicCounter.get());
    }

    private synchronized void inc() {
       atomicCounter.incrementAndGet();
    }
}

class DeadlockExample {

    private static int account = 100;
    private static final Object WITHDRAW_LOCK = new Object();
    private static final Object DEPOSIT_LOCK = new Object();

    public static void main(String[] args) {
        System.out.println(account);
        DeadlockExample deadlockExample = new DeadlockExample();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            createThreadsWithdraw(deadlockExample, threads);
        }
        for (int i = 0; i < 50; i++) {
            createThreadsDeposit(deadlockExample, threads);
        }
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println(account);
    }

    private static void createThreadsWithdraw(DeadlockExample deadlockExample, List<Thread> threads) {
        Thread thread = new Thread(deadlockExample::withdraw);
        thread.start();
        threads.add(thread);
    }

    private static void createThreadsDeposit(DeadlockExample deadlockExample, List<Thread> threads) {
        Thread thread = new Thread(deadlockExample::deposit);
        thread.start();
        threads.add(thread);
    }

    private void withdraw() {
        synchronized (WITHDRAW_LOCK) {
            account--;
            synchronized (DEPOSIT_LOCK) {
                System.out.println(account);
            }
        }
    }

    private void deposit() {
        synchronized (DEPOSIT_LOCK) {
            account++;
            synchronized (WITHDRAW_LOCK) {
                System.out.println(account);
            }
        }
    }


}
