import java.util.*;
import java.util.stream.Collectors;

public class Service {
    private Map<Integer, Student> studentMap = new HashMap<>();

    public void addStudent(Student s) {
        if (studentMap.containsKey(s.getId())) {
            System.out.println("Error: Student with ID " + s.getId() + " already exists.");
            return;
        }
        studentMap.put(s.getId(), s);
    }

    public Student findById(int id) {
        return studentMap.get(id);
    }

    public void removeStudent(int id) {
        studentMap.remove(id);
    }

    public List<Student> getAllSortedByName() {
        return studentMap.values().stream()
                .sorted(Comparator.comparing(Student::getName))
                .collect(Collectors.toList());
    }

    public List<Student> filterByNamePrefix(String prefix) {
        return studentMap.values().stream()
                .filter(s -> s.getName().startsWith(prefix))
                .collect(Collectors.toList());
    }
}