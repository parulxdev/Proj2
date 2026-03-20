package service;

import model.Student;
import exception.*;

import java.util.*;

public class StudentService {

    private Map<Integer, Student> studentMap = new HashMap<>();

    public void addStudent(Student student) {

        if (studentMap.containsKey(student.getId())) {
            throw new DuplicateStudentException(
                "Student already exists with id: " + student.getId()
            );
        }

        if (!student.getEmail().contains("@")) {
            throw new InvalidEmailException(
                "Invalid email: " + student.getEmail()
            );
        }

        studentMap.put(student.getId(), student);
    }

    public Student findById(int id) {
        if (!studentMap.containsKey(id)) {
            throw new StudentNotFoundException(
                "Student not found with id: " + id
            );
        }
        return studentMap.get(id);
    }

    public void deleteStudent(int id) {
        if (!studentMap.containsKey(id)) {
            throw new StudentNotFoundException(
                "Cannot delete. Student not found with id: " + id
            );
        }
        studentMap.remove(id);
    }

    public void updateStudent(int id, String newEmail) {
        if (!studentMap.containsKey(id)) {
            throw new StudentNotFoundException(
                "Cannot update. Student not found with id: " + id
            );
        }

        if (!newEmail.contains("@")) {
            throw new InvalidEmailException("Invalid email: " + newEmail);
        }

        studentMap.get(id).setEmail(newEmail);
    }

    public List<Student> getAllSortedByName() {
        return studentMap.values()
                .stream()
                .sorted(Comparator.comparing(Student::getName))
                .toList();
    }

    public List<Student> filterByNamePrefix(String prefix) {
        return studentMap.values()
                .stream()
                .filter(s -> s.getName().startsWith(prefix))
                .toList();
    }
}