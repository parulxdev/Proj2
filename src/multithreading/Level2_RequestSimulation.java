package multithreading;

import java.util.Random;

public class Level2_RequestSimulation {
    
    static class RequestTask implements Runnable {
        private int requestId;
        private long processingTime;
        
        public RequestTask(int requestId) {
            this.requestId = requestId;
            this.processingTime = 500 + new Random().nextInt(1500);
        }
        
        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            long startTime = System.currentTimeMillis();
            
            System.out.printf("[%s] 🚀 Started processing request %d (est: %dms)%n",
                threadName, requestId, processingTime);
            
            try {
                Thread.sleep(processingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            long duration = System.currentTimeMillis() - startTime;
            System.out.printf("[%s] ✅ Completed request %d (took: %dms)%n",
                threadName, requestId, duration);
        }
    }
    
    public static void main(String[] args) {
        System.out.println("\n=== LEVEL 2: Simulating 10 Concurrent API Requests ===\n");
        
        long startTime = System.currentTimeMillis();
        Thread[] threads = new Thread[10];
        
        // Simulate 10 users hitting the API simultaneously
        for (int i = 1; i <= 10; i++) {
            threads[i-1] = new Thread(new RequestTask(i), "Request-Handler-" + i);
            threads[i-1].start();
        }
        
        // Wait for all to complete
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        long totalTime = System.currentTimeMillis() - startTime;
        System.out.printf("\n📊 All 10 requests completed in %dms%n", totalTime);
        System.out.println("💡 Key Takeaway: Total time ≈ longest request (not sum of all) because they run concurrently!\n");
    }
}