package ru.hogwarts.school_test.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school_test.model.Student;

import java.util.Collection;

public interface StudentRepository extends JpaRepository <Student, Long> {
    // Метод для поиска студентов по диапазону возраста
    Collection<Student> findByAgeBetween(int minAge, int maxAge);
    // Метод для поиска студентов по факультету
    Collection<Student> findByFacultyId(Long facultyId);
}