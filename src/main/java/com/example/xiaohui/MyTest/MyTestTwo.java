package com.example.xiaohui.MyTest;

public class MyTestTwo {
    public static void main(String[] args) {
        int numThreads = Runtime.getRuntime().availableProcessors();
        System.out.println("系统的CPU核心数: " + numThreads);

        // 启动多个线程
        for (int i = 0; i < numThreads; i++) {
            new Thread(new CPULoadTask()).start();
        }
    }

    // 计算任务
    static class CPULoadTask implements Runnable {
        @Override
        public void run() {
            while (true) {
                // 执行Fibonacci计算
                fibonacci(40); // 计算较大的Fibonacci数
            }
        }

        // 递归计算Fibonacci数列
        private long fibonacci(int n) {
            if (n <= 1) {
                return n;
            }
            return fibonacci(n - 1) + fibonacci(n - 2);
        }
    }
}
