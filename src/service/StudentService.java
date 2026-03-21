package service;

import model.Student;
import exception.*;

import java.util.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;


public class StudentService {

    private final ConcurrentHashMap<Integer, Student> studentMap = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private int totalOperations = 0;
    private synchronized void incrementOperationCount() {
        totalOperations++;
    }
    public synchronized int getTotalOperations() {
        return totalOperations;
    }
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
        lock.writeLock().lock();

        try{
            studentMap.put(student.getId(), student);
            incrementOperationCount();
            System.out.println(Thread.currentThread().getName() + " - Added student " + student.getId());
        } 
        finally {
            lock.writeLock().unlock();
        }
    }

    public Student findById(int id) {
        if (!studentMap.containsKey(id)) {
            throw new StudentNotFoundException(
                "Student not found with id: " + id
            );
        }
        incrementOperationCount();
        return studentMap.get(id);
    }

    public void deleteStudent(int id) {
        lock.writeLock().lock();
        try{
            if (!studentMap.containsKey(id)) {
                throw new StudentNotFoundException(
                    "Cannot delete. Student not found with id: " + id
                );
            }
            incrementOperationCount();
            studentMap.remove(id);
        }
        finally {
            lock.writeLock().unlock();
        }
        
    }
    public int getSize() {
        return studentMap.size();
    }

    public void updateStudent(int id, String newEmail) {
        
        if (!newEmail.contains("@")) {
            throw new InvalidEmailException("Invalid email: " + newEmail);
        }
        lock.writeLock().lock();
        try{
            if (!studentMap.containsKey(id)) {
            throw new StudentNotFoundException(
                "Cannot update. Student not found with id: " + id);
            }
        
            incrementOperationCount();
            studentMap.get(id).setEmail(newEmail);
        }
        finally {
            lock.writeLock().unlock();
        }
    }

    public List<Student> getAllSortedByName() {
        lock.readLock().lock();
        try {
            return studentMap.values().stream()
                    .sorted(Comparator.comparing(Student::getName))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Student> filterByNamePrefix(String prefix) {
        lock.readLock().lock();
        try {
            return studentMap.values().stream()
                    .filter(s -> s.getName().startsWith(prefix))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
    public void clear() {
        lock.writeLock().lock();
        try {
            studentMap.clear();
            System.out.println("Cleared all students");
        } finally {
            lock.writeLock().unlock();
        }
    }
    public List<Student> getAllStudents() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(studentMap.values());
        } finally {
            lock.readLock().unlock();
        }
    }
}