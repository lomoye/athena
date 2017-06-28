//package com.lomoye.common.concurrent;
//
//import org.junit.Test;
//
///**
// * Created by tommy on 2016/7/11.
// */
//public class TaskQueueTest {
//
//    @Test
//    public void testTake() throws Exception {
//        TaskQueue<String> queue = new TaskQueue<String>() {
//            @Override
//            protected String getQueueName() {
//                return "test-queue";
//            }
//
//            @Override
//            protected boolean isSameClass(String t1, String t2) {
//                return true;
//            }
//
//            @Override
//            protected boolean supportMultiplePerClass() {
//                return true;
//            }
//        };
//
////        new Producer(queue).start();
////
////        for (int i = 0; i < 2; i++) {
////            new Consumer(queue, i).start();
////        }
//    }
//}
//
//class Producer extends Thread {
//    TaskQueue<String> queue;
//
//    public Producer(TaskQueue<String> queue) {
//        this.queue = queue;
//    }
//
//    @Override
//    public void run() {
//        for (long i = 0; i < 1000 * 1000L; i++) {
//            queue.put("" + i, false);
//        }
//    }
//}
//
//class Consumer extends Thread {
//    TaskQueue<String> queue;
//    int num;
//
//    public Consumer(TaskQueue<String> queue, int num) {
//        this.queue = queue;
//        this.num = num;
//    }
//
//    @Override
//    public void run() {
//        setName("consumer-" + num);
//        while (true) {
//            String s = queue.take(false);
//            System.out.println("thread-" + num + "|s=" + s);
//            //ThreadUtil.safeSleep(30, TimeUnit.SECONDS, "");
//            queue.finish(s);
//        }
//    }
//}
