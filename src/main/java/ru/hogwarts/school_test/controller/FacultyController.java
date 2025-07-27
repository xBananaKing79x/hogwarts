package ru.hogwarts.school_test.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school_test.model.Faculty;
import ru.hogwarts.school_test.service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("/api/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    // создание факультета
    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    // получение факультета по ID
    @GetMapping("/{id}")
    public Faculty getFaculty(@PathVariable Long id) {
        return facultyService.getFacultyById(id);
    }

    // обновление факультета
    @PutMapping
    public Faculty updateFaculty(@RequestBody Faculty faculty) {
        return facultyService.updateFaculty(faculty);
    }

    // DELETE - удаление факультета
    @DeleteMapping("/{id}")
    public void deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
    }

    // READ ALL - получение всех факультетов
    @GetMapping
    public Collection<Faculty> getAllFaculties() {
        return facultyService.getAllFaculties();
    }

    // Фильтрация факультетов по цвету (возвращен к исходному значению)
    @GetMapping("/color/{color}")
    public Collection<Faculty> getFacultiesByColor(@PathVariable String color) {
        return facultyService.getFacultiesByColor(color);
    }
}
