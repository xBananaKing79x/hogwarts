package ru.hogwarts.school_test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school_test.model.Faculty;

import java.util.Collection;

public interface FacultyRepository extends JpaRepository {
    // Метод для поиска факультетов по имени или цвету, игнорируя регистр
    Collection<Faculty> findByNameOrColorIgnoreCase(String name, String color);
}

