package multithreading;

import service.StudentService;
import model.Student;

import java.util.concurrent.*;

public class PerformanceComparison {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("\n=== PERFORMANCE COMPARISON: Single-threaded vs Multi-threaded ===\n");
        
        // Single-threaded test
        System.out.println("1. SINGLE-THREADED OPERATIONS:");
        long singleTime = testSingleThreaded();
        System.out.printf("   Time: %dms%n%n", singleTime);
        
        // Multi-threaded test with thread pool
        System.out.println("2. MULTI-THREADED OPERATIONS (Thread Pool):");
        long multiTime = testMultiThreaded();
        System.out.printf("   Time: %dms%n%n", multiTime);
        
        // Speedup calculation
        double speedup = (double) singleTime / multiTime;
        System.out.printf("🚀 SPEEDUP: %.2fx faster with multithreading!%n", speedup);
        System.out.println("💡 This is why servers use thread pools for handling multiple users!\n");
    }
    
    private static long testSingleThreaded() {
        StudentService service = new StudentService();
        long startTime = System.currentTimeMillis();
        
        // Add 1000 students sequentially
        for (int i = 1; i <= 1000; i++) {
            Student s = new Student(i, "Student-" + i, "student" + i + "@test.com");
            try {
                service.addStudent(s);
            } catch (Exception e) {
                // Ignore duplicates
            }
        }
        
        return System.currentTimeMillis() - startTime;
    }
    
    private static long testMultiThreaded() throws InterruptedException {
        StudentService service = new StudentService();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        long startTime = System.currentTimeMillis();
        
        CountDownLatch latch = new CountDownLatch(1000);
        
        // Add 1000 students concurrently
        for (int i = 1; i <= 1000; i++) {
            final int id = i;
            executor.submit(() -> {
                Student s = new Student(id, "Student-" + id, "student" + id + "@test.com");
                try {
                    service.addStudent(s);
                } catch (Exception e) {
                    // Ignore duplicates
                }
                latch.countDown();
            });
        }
        
        latch.await();
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        
        return System.currentTimeMillis() - startTime;
    }
}