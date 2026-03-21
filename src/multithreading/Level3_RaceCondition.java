package multithreading;

import java.util.concurrent.CountDownLatch;

public class Level3_RaceCondition {
    
    static class UnsafeCounter {
        private int count = 0;
        
        // THIS IS NOT THREAD-SAFE!
        public void increment() {
            // This operation is NOT atomic - causes race conditions
            count++;
        }
        
        public int getCount() {
            return count;
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("\n=== LEVEL 3: Race Condition Demonstration ===\n");
        System.out.println("Expected: 100,000");
        System.out.println("Actual: ??? (Will be less and random)\n");
        
        UnsafeCounter counter = new UnsafeCounter();
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
        System.out.printf("Lost increments: %d%n", expected - actual);
        
        System.out.println("\n⚠️  Race Condition Detected! Multiple threads corrupted shared data!");
        System.out.println("💡 Key Takeaway: HashMap and simple int operations are NOT thread-safe!\n");
    }
}