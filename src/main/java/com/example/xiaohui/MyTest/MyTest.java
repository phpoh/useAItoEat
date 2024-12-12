package com.example.xiaohui.MyTest;

public class MyTest {
    public static void main(String[] args) {
        // 获取系统的CPU核心数
        int numThreads = Runtime.getRuntime().availableProcessors();
        System.out.println("系统的CPU核心数: " + numThreads);

        // 创建并启动多个线程，每个线程都会占用CPU
        for (int i = 0; i < numThreads; i++) {
            new Thread(new CPULoadTask()).start();
        }
    }

    // 计算任务
    static class CPULoadTask implements Runnable {
        @Override
        public void run() {
            while (true) {
                // 执行一个复杂的计算任务来占用CPU
                Math.sqrt(Math.random());
            }
        }
    }
}
