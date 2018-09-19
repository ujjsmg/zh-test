package com.myfirstspring.toutiao;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class MyThread extends Thread{
    private int tid;
    public MyThread(int tid){
        this.tid = tid;
    }
    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(500);
                System.out.println(String.format("Thread%d:%d", tid, i));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

class Producer implements Runnable{
    private BlockingQueue<String> queue;
    public Producer(BlockingQueue<String> queue){
        this.queue = queue;
    }
    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(500);
                queue.put(String.valueOf(i));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable{
    private BlockingQueue<String> queue;
    public Consumer(BlockingQueue<String> queue){
        this.queue = queue;
    }
    @Override
    public void run() {
        try {
            while (true) {
                System.out.println(Thread.currentThread().getName() + ":" + queue.take());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

public class MultiThread {
    public static void sleep(int millis){
        try{
            Thread.sleep(millis);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void testThread(){
        //for(int i = 0; i < 10; i++)
            //new MyThread(i).start();

        for(int i = 0; i < 10; i++) {
            final int tid = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 10; j++) {
                        sleep(500);
                        System.out.println(String.format("Thread2%d:%d", tid, j));
                    }
                }
            }).start();
        }
    }

    private static Object obj = new Object();

    public static void testSynchronized1(){
        synchronized (obj){
            for(int i = 0; i < 10; i++){
                sleep(500);
                System.out.println(String.format("Thread3:%d", i));
            }
        }
    }
    public static void testSynchronized2(){
        synchronized (new Object()){
            for(int i = 0; i < 10; i++){
                sleep(500);
                System.out.println(String.format("Thread4:%d", i));
            }
        }
    }
    public static void testSynchronized(){
        for(int i = 0; i < 10; i++)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    testSynchronized1();
                    testSynchronized2();
                }
            }).start();
    }

    public static void testBlockingQueue(){
        BlockingQueue<String> queue = new ArrayBlockingQueue<String>(10);
        new Thread(new Producer(queue)).start();
        new Thread(new Consumer(queue), "Consumer1").start();
        new Thread(new Consumer(queue), "Consumer2").start();
    }

    private static int count = 0;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    public static void testWithAtomic(){
        for(int i = 0; i < 10; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int j = 0; j < 10; j++){
                        sleep(500);
                        System.out.println(atomicInteger.incrementAndGet());
                    }
                }
            }).start();
        }
    }
    public static void testWithoutAtomic(){
        for(int i = 0; i < 10; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sleep(500);
                    for(int j = 0; j < 10; j++){
                        count++;
                        System.out.println(count);
                    }
                }
            }).start();
        }
    }
    public static void testAtomic(){
        //testWithAtomic();
        testWithoutAtomic();
    }
    private static ThreadLocal<Integer> threadLocalUserId = new ThreadLocal<Integer>();
    private static int userId;
    public static void testThreadLocal(){
        for(int i = 0; i < 10; i++){
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    threadLocalUserId.set(finalI);
                    sleep(500);
                    System.out.println("ThreadLocal:" + threadLocalUserId.get());
                }
            }).start();
        }

        for(int i = 0; i < 10; i++){
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    userId = finalI;
                    sleep(500);
                    System.out.println("NonThreadLocal:" + userId);
                }
            }).start();
        }
    }
    public static void testExecutor(){
        //ExecutorService service = Executors.newSingleThreadExecutor();
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 10; i++){
                    sleep(500);
                    System.out.println("Execute1 " + i);
                }
            }
        });
        service.submit(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 10; i++){
                    sleep(500);
                    System.out.println("Execute2 " + i);
                }
            }
        });
        service.shutdown();
        while (!service.isTerminated()){
            sleep(500);
            System.out.println("Wait for terminate.");
        }
    }
    public static void testFuture(){
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Integer> future = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                sleep(500);
                return 1;
            }
        });
        service.shutdown();
        try {
            //System.out.println(future.get());
            System.out.println(future.get(100, TimeUnit.MILLISECONDS));
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public static void main(String[] args){
        //testThread();
        //testSynchronized();
        //testBlockingQueue();
        //testAtomic();
        //testThreadLocal();
        //testExecutor();
        testFuture();
    }
}
