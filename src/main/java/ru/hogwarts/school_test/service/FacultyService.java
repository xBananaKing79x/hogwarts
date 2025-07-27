package ru.hogwarts.school_test.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school_test.model.Faculty;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {
private long idCounter = 1;
    private final HashMap<Long, Faculty> faculties = new HashMap <> ();

    // создание факультета
    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(idCounter++);
        faculties.put(idCounter, faculty);
        return faculty;
    }

    // получение факультета по ID
    public Faculty getFacultyById(long id) {
        return faculties.get(id);
    }

    // получение всех факультетов
    public Collection<Faculty> getAllFaculties() {
        return faculties.values();
    }

    // обновление факультета
    public Faculty updateFaculty(Faculty faculty) {
            faculties.put(faculty.getId(), faculty);
            return faculty;
    }

    // удаление факультета
    public Faculty deleteFaculty(long id) {
        return faculties.remove(id);
    }
    // Фильтрация факультетов по цвету
    public Collection<Faculty> getFacultiesByColor(String color) {
        return faculties.values().stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .collect(Collectors.toList());
    }
}
