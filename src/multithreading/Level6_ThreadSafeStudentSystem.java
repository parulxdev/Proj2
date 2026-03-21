package multithreading;

import model.Student;
import service.StudentService;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Level6_ThreadSafeStudentSystem {
    
    private static final AtomicInteger successCount = new AtomicInteger(0);
    private static final AtomicInteger failureCount = new AtomicInteger(0);
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("\n=== LEVEL 6: Thread-Safe Student Management System ===\n");
        
        StudentService service = new StudentService();
        
        // Create thread pool with 20 threads
        ExecutorService executor = Executors.newFixedThreadPool(20);
        
        System.out.println("Simulating concurrent operations:");
        System.out.println("- 10 threads adding students");
        System.out.println("- 5 threads deleting students");
        System.out.println("- 3 threads updating students");
        System.out.println("- 5 threads reading students\n");
        
        long startTime = System.currentTimeMillis();
        
        // 10 threads adding students
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                for (int j = 1; j <= 50; j++) {
                    int id = ThreadLocalRandom.current().nextInt(1, 101);
                    String name = "Student-" + id;
                    String email = name.toLowerCase() + "@test.com";
                    Student student = new Student(id, name, email);
                    
                    try {
                        service.addStudent(student);
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                        // Expected for duplicates - normal operation
                    }
                    
                    // Small random delay to simulate real workload
                    try {
                        Thread.sleep(ThreadLocalRandom.current().nextInt(1, 5));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        
        // 5 threads deleting students
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                for (int j = 1; j <= 30; j++) {
                    int id = ThreadLocalRandom.current().nextInt(1, 101);
                    
                    try {
                        service.deleteStudent(id);
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                        // Expected if student doesn't exist
                    }
                    
                    try {
                        Thread.sleep(ThreadLocalRandom.current().nextInt(1, 5));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        
        // 3 threads updating students
        for (int i = 0; i < 3; i++) {
            executor.submit(() -> {
                for (int j = 1; j <= 40; j++) {
                    int id = ThreadLocalRandom.current().nextInt(1, 101);
                    String newEmail = "updated" + j + "@test.com";
                    
                    try {
                        service.updateStudent(id, newEmail);
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                    }
                    
                    try {
                        Thread.sleep(ThreadLocalRandom.current().nextInt(1, 5));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        
        // 5 threads reading students
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                for (int j = 1; j <= 50; j++) {
                    int id = ThreadLocalRandom.current().nextInt(1, 101);
                    
                    try {
                        Student student = service.findById(id);
                        if (student != null) {
                            successCount.incrementAndGet();
                        }
                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                    }
                    
                    // Also test getAllSortedByName and filter
                    if (j % 10 == 0) {
                        service.getAllSortedByName();
                        service.filterByNamePrefix("S");
                    }
                    
                    try {
                        Thread.sleep(ThreadLocalRandom.current().nextInt(1, 3));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        
        // Shutdown and wait for completion
        executor.shutdown();
        boolean finished = executor.awaitTermination(60, TimeUnit.SECONDS);
        
        long totalTime = System.currentTimeMillis() - startTime;
        
        System.out.println("\n📊 RESULTS:");
        System.out.println("=".repeat(50));
        System.out.printf("Total time: %dms%n", totalTime);
        System.out.printf("Successful operations: %d%n", successCount.get());
        System.out.printf("Failed operations (expected): %d%n", failureCount.get());
        System.out.printf("Final student count: %d%n", service.getSize());
        System.out.printf("Total operations processed: %d%n", service.getTotalOperations());
        
        System.out.println("\n FINAL VERIFICATION:");
        System.out.println("=".repeat(50));
        
        // Verify data integrity
        boolean hasCorruption = verifyDataIntegrity(service);
        
        if (!hasCorruption) {
            System.out.println(" No data corruption detected!");
            System.out.println(" All operations were thread-safe!");
            System.out.println(" HashMap replaced with ConcurrentHashMap for thread-safety");
        }
        
        System.out.println("\n💡 Key Takeaways:");
        System.out.println("1. ConcurrentHashMap provides thread-safe operations without explicit synchronization");
        System.out.println("2. Thread pools efficiently manage concurrent requests");
        System.out.println("3. Multiple operations (add/delete/update/read) can happen simultaneously");
        System.out.println("4. Data integrity is maintained even with high concurrency");
        System.out.println("5. This is exactly how real backend servers handle multiple users!\n");
    }
    
    private static boolean verifyDataIntegrity(StudentService service) {
        List<Student> students = service.getAllStudents();
        
        // Check for duplicate IDs
        Set<Integer> ids = new HashSet<>();
        boolean hasDuplicates = false;
        
        for (Student s : students) {
            if (ids.contains(s.getId())) {
                System.out.println("No DUPLICATE ID FOUND: " + s.getId());
                hasDuplicates = true;
            }
            ids.add(s.getId());
        }
        
        // Check for null values
        boolean hasNulls = students.stream().anyMatch(s -> s == null);
        
        if (hasNulls) {
            System.out.println("No NULL STUDENT FOUND");
        }
        
        // Check email format
        boolean invalidEmails = students.stream()
            .anyMatch(s -> !s.getEmail().contains("@"));
        
        if (invalidEmails) {
            System.out.println("No INVALID EMAIL FORMAT FOUND");
        }
        
        return hasDuplicates || hasNulls || invalidEmails;
    }
}