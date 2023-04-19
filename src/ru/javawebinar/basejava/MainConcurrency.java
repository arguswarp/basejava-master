package ru.javawebinar.basejava;

import java.util.ArrayList;
import java.util.List;

public class MainConcurrency {
    public static final int THREADS_NUMBER = 10000;
    private static volatile int counter;
    private static final Object LOCK = new Object();

    public static void main(String[] args) throws InterruptedException {
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
        List<Thread> threads = new ArrayList<>(THREADS_NUMBER);
        for (int i = 0; i < THREADS_NUMBER; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    mainConcurrency.inc();
                }
            });
            thread.start();
            threads.add(thread);
        }
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println(counter);
    }

    private synchronized void inc() {
        counter++;
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
