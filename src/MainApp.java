import model.Student;
import service.StudentService;
import exception.*;

import java.util.*;

public class MainApp {

    public static void main(String[] args) {

        // 🔹 HashSet duplicate test
        Set<Student> studentSet = new HashSet<>();
        Student s1 = new Student(101, "Alice", "alice@test.com");
        Student s1Duplicate = new Student(101, "Alice", "alice@test.com");

        studentSet.add(s1);
        studentSet.add(s1Duplicate);

        System.out.println("Set Size (Should be 1): " + studentSet.size());

        // 🔹 Service usage
        StudentService service = new StudentService();

        try {
            service.addStudent(s1);
            service.addStudent(new Student(102, "Bob", "bob@test.com"));
            service.addStudent(new Student(103, "Alex", "alex@test.com"));

            service.addStudent(new Student(101, "Duplicate", "dup@test.com"));

        } catch (DuplicateStudentException e) {
            System.out.println(e.getMessage());
        }

        try {
            service.addStudent(new Student(104, "Invalid", "wrong-email"));
        } catch (InvalidEmailException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println("\nSearch ID 102: " + service.findById(102));
            System.out.println("Search ID 999: " + service.findById(999)); // No
        } catch (StudentNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            service.updateStudent(102, "newbob@test.com");
            service.updateStudent(999, "fail@test.com"); // No
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
    }
}