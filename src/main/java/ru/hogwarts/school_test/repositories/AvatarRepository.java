package ru.hogwarts.school_test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school_test.model.Avatar;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    Optional<Avatar> findByStudentId(Long studentId);
}
