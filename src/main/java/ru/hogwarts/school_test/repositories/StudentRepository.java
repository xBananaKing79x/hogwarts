package ru.hogwarts.school_test.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school_test.model.Student;

import java.util.Collection;

public interface StudentRepository extends JpaRepository <Student, Long> {
    // Метод для поиска студентов по диапазону возраста
    Collection<Student> findByAgeBetween(int minAge, int maxAge);
    // Метод для поиска студентов по факультету
    Collection<Student> findByFacultyId(Long facultyId);

    // Получить количество всех студентов в школе
    @Query("SELECT COUNT(s) FROM Student s")
    int getCountOfAllStudents();

    // Получить средний возраст студентов
    @Query("SELECT AVG(s.age) FROM Student s")
    double getAverageAgeOfStudents();

    // Получить пять последних студентов (с наибольшими ID)
    @Query("SELECT s FROM Student s ORDER BY s.id DESC LIMIT 5")
    Collection<Student> getLastFiveStudents();
}