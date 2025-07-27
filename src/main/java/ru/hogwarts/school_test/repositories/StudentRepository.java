package ru.hogwarts.school_test.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school_test.model.Student;

public interface StudentRepository extends JpaRepository <Student, Long> {
}