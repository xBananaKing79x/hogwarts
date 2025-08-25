package ru.hogwarts.school_test.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school_test.model.Faculty;
import ru.hogwarts.school_test.model.Student;
import ru.hogwarts.school_test.repositories.FacultyRepository;
import ru.hogwarts.school_test.repositories.StudentRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    // создание факультета
    public Faculty createFaculty(Faculty faculty) {

        return (Faculty) facultyRepository.save(faculty);
    }

    // получение факультета по ID
    public Faculty getFacultyById(long id) {

        return (Faculty) facultyRepository.findAllById(Collections.singleton(id));
    }

    // получение всех факультетов
    public Collection<Faculty> getAllFaculties() {

        return facultyRepository.findAll();
    }

    // обновление факультета
    public Faculty updateFaculty(Faculty faculty) {

        return (Faculty) facultyRepository.save(faculty);
    }

    // удаление факультета
    public void deleteFaculty(long id) {
        facultyRepository.deleteById(id);
    }

    // Поиск факультетов по имени или цвету, игнорируя регистр
    public Collection<Faculty> getFacultiesByNameOrColorIgnoreCase(String nameOrColor) {
        return facultyRepository.findByNameOrColorIgnoreCase(nameOrColor, nameOrColor);
    }

    // Получить студентов факультета
    public Collection<Student> getStudentsByFacultyId(Long facultyId) {
        return studentRepository.findByFacultyId(facultyId);

    }

    // Фильтрация факультетов по цвету
    public Collection<Faculty> getFacultiesByColor(String color) {
        return facultyRepository.findAll().stream()
                .filter((Faculty)faculty -> faculty.getColor().equals(color))
                .collect(Collectors.toList());
    }
}

