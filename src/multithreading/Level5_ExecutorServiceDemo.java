package multithreading;

import java.util.concurrent.*;

public class Level5_ExecutorServiceDemo {
    
    static class RequestTask implements Runnable {
        private int requestId;
        
        public RequestTask(int requestId) {
            this.requestId = requestId;
        }
        
        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            System.out.printf("[%s] Processing request %d%n", threadName, requestId);
            
            try {
                // Simulate processing time
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            System.out.printf("[%s] Completed request %d%n", threadName, requestId);
        }
    }
    
    public static void main(String[] args) {
        System.out.println("\n=== LEVEL 5: ExecutorService (Thread Pool) ===\n");
        
        // Create thread pool with 5 threads
        ExecutorService executor = Executors.newFixedThreadPool(5);
        
        System.out.println("Pool size: 5 threads");
        System.out.println("Total tasks: 20\n");
        
        long startTime = System.currentTimeMillis();
        
        // Submit 20 tasks
        for (int i = 1; i <= 20; i++) {
            executor.submit(new RequestTask(i));
        }
        
        // Graceful shutdown
        executor.shutdown();
        
        try {
            executor.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        long totalTime = System.currentTimeMillis() - startTime;
        System.out.printf("\n All 20 requests completed in %dms%n", totalTime);
        System.out.println("💡 Key Takeaway: Thread pools reuse threads, control load, and prevent resource exhaustion!\n");
    }
}