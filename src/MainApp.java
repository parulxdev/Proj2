import java.util.*;
public class MainApp {
    public static void main(String[] args) {
        Set<Student> studentSet = new HashSet<>();
        Student s1 = new Student(101, "Alice", "alice@test.com");
        Student s1Duplicate = new Student(101, "Alice", "alice@test.com");

        studentSet.add(s1);
        studentSet.add(s1Duplicate); 
        
        System.out.println("Set Size (Should be 1): " + studentSet.size());

        Service service = new Service();
        service.addStudent(s1);
        service.addStudent(new Student(102, "Bob", "bob@test.com"));
        service.addStudent(new Student(105, "Charlie", "charlie@test.com"));
        service.addStudent(new Student(103, "Alex", "alex@test.com"));

        System.out.println("Search ID 102: " + service.findById(102));

        System.out.println("\n--- Students Sorted by Name ---");
        service.getAllSortedByName().forEach(System.out::println);

        System.out.println("\n--- Students starting with A");
        service.filterByNamePrefix("A").forEach(System.out::println);
    }
}