package ru.hogwarts.school_test.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school_test.model.Faculty;
import ru.hogwarts.school_test.repositories.FacultyRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    // создание факультета
    public Faculty createFaculty(Faculty faculty) {

        return (Faculty) facultyRepository.save(faculty);
    }

    // получение факультета по ID
    public Faculty getFacultyById(long id) {

        return (Faculty) facultyRepository.getById(id);
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
    // Фильтрация факультетов по цвету
    public Collection<Faculty> getFacultiesByColor(String color) {
        return faculties.values().stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .collect(Collectors.toList());
    }
}
