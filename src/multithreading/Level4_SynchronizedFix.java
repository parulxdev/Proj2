package multithreading;

import java.util.concurrent.CountDownLatch;

public class Level4_SynchronizedFix {
    
    static class SafeCounter {
        private int count = 0;
        
        // synchronized ensures atomic operation
        public synchronized void increment() {
            count++;
        }
        
        public synchronized int getCount() {
            return count;
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("\n=== LEVEL 4: Fixing Race Condition with synchronized ===\n");
        System.out.println("Expected: 100,000");
        System.out.println("Actual: 100,000 (Always correct!)\n");
        
        SafeCounter counter = new SafeCounter();
        int threadCount = 100;
        int incrementsPerThread = 1000;
        
        CountDownLatch latch = new CountDownLatch(threadCount);
        Thread[] threads = new Thread[threadCount];
        
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    counter.increment();
                }
                latch.countDown();
            });
            threads[i].start();
        }
        
        latch.await();
        
        int expected = threadCount * incrementsPerThread;
        int actual = counter.getCount();
        
        System.out.printf("Expected: %d%n", expected);
        System.out.printf("Actual: %d%n", actual);
        
        System.out.println("\n Data consistency maintained! synchronized ensures mutual exclusion!");
        System.out.println("💡 Key Takeaway: Use synchronized or thread-safe collections for shared data!\n");
    }
}