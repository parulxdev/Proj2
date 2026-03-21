import model.Student;
import multithreading.Level1_BasicThreads;
import multithreading.Level2_RequestSimulation;
import multithreading.Level3_RaceCondition;
import multithreading.Level4_SynchronizedFix;
import multithreading.Level5_ExecutorServiceDemo;
import multithreading.Level6_ThreadSafeStudentSystem;
import service.StudentService;
import exception.*;

import java.util.*;

public class MainApp {

    public static void main(String[] args) {
        
        
        

        //  HashSet duplicate test
        Set<Student> studentSet = new HashSet<>();
        Student s1 = new Student(101, "Alice", "alice@test.com");
        Student s1Duplicate = new Student(101, "Alice", "alice@test.com");

        studentSet.add(s1);
        studentSet.add(s1Duplicate);

        System.out.println("Set Size (Should be 1): " + studentSet.size());

        //  Service usage
        StudentService service = new StudentService();

        try {
            service.addStudent(s1);
            service.addStudent(new Student(102, "Bob", "bob@test.com"));
            service.addStudent(new Student(103, "Alex", "alex@test.com"));

            //  duplicate
            service.addStudent(new Student(101, "Duplicate", "dup@test.com"));

        } catch (DuplicateStudentException e) {
            System.out.println(e.getMessage());
        }

        try {
            //  invalid email
            service.addStudent(new Student(104, "Invalid", "wrong-email"));
        } catch (InvalidEmailException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println("\nSearch ID 102: " + service.findById(102));
            System.out.println("Search ID 999: " + service.findById(999)); // ❌
        } catch (StudentNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            service.updateStudent(102, "newbob@test.com");
            service.updateStudent(999, "fail@test.com"); // ❌
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n--- Students Sorted by Name ---");
        service.getAllSortedByName().forEach(System.out::println);

        System.out.println("\n--- Students starting with A ---");
        service.filterByNamePrefix("A").forEach(System.out::println);

        // finally demo
        try {
            int a = 10, b = 0;
            int result = a / b;
        } catch (ArithmeticException e) {
            System.out.println("\nCannot divide by zero");
        } finally {
            System.out.println("Execution completed");
        }
        // Run all multithreading levels
        System.out.println("\n" + "=".repeat(60));
        System.out.println("MULTITHREADING DEMONSTRATION");
        System.out.println("=".repeat(60) + "\n");
        
        try {
            // Level 1: Basic Thread Creation
            Level1_BasicThreads.main(new String[]{});
            
            // Level 2: Request Simulation
            Level2_RequestSimulation.main(new String[]{});
            
            // Level 3: Race Condition
            Level3_RaceCondition.main(new String[]{});
            
            // Level 4: Synchronized Fix
            Level4_SynchronizedFix.main(new String[]{});
            
            // Level 5: ExecutorService
            Level5_ExecutorServiceDemo.main(new String[]{});
            
            // Level 6: Thread-Safe Student System
            Level6_ThreadSafeStudentSystem.main(new String[]{});
        } catch (InterruptedException e) {
            System.out.println("Multithreading execution was interrupted: " + e.getMessage());
        }
    }
}