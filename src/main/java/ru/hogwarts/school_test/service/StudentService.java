package ru.hogwarts.school_test.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.hogwarts.school_test.model.Faculty;
import ru.hogwarts.school_test.model.Student;
import ru.hogwarts.school_test.repositories.StudentRepository;

import java.util.Collection;


@Service
public class StudentService {

    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // создание студента
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    // получение студента по ID
    public Student getStudentById(long id) {
        return studentRepository.findById(id).get();
    }

    // получение всех студентов
    public Collection<Student> getAllStudents() {

        return studentRepository.findAll();
    }

    // обновление студента
    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }

    // удаление студента
    public void deleteStudent(long id) {
        studentRepository.deleteById(id);
    }

    // Получение студентов по диапазону возраста
    public Collection<Student> getStudentsByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }
    // Получить факультет студента
    public Faculty getFacultyByStudentId(Long studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        return student != null ? student.getFaculty() : null;
    }
}

