package ru.hogwarts.school_test.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school_test.model.Faculty;
import ru.hogwarts.school_test.model.Student;
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
    @GetMapping ("/{id}") // GET http://localhost:8080/Faculty/id
    public ResponseEntity<Faculty> getFacultyInfo(@PathVariable long id) {
        Faculty faculty = facultyService.getFacultyById(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    // обновление факультета
    @PutMapping
    public ResponseEntity<Faculty> updateFaculty(@RequestBody Faculty faculty) {
        Faculty updateFaculty = facultyService.updateFaculty(faculty);
        if (updateFaculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(updateFaculty);
    }

    // DELETE - удаление факультета
    @DeleteMapping("/{id}")
    public ResponseEntity deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    // READ ALL - получение всех факультетов
    @GetMapping
    public ResponseEntity<Collection<Faculty>> getAllFaculties() {
        return ResponseEntity.ok(facultyService.getAllFaculties());
    }

    // Поиск факультетов по имени или цвету, игнорируя регистр
    @GetMapping("/search")
    public Collection<Faculty> getFacultiesByNameOrColorIgnoreCase(@RequestParam("query") String query) {
        return facultyService.getFacultiesByNameOrColorIgnoreCase(query);
    }

    // Получить студентов факультета
    @GetMapping("/{id}/students")
    public Collection<Student> getStudentsByFaculty(@PathVariable Long id) {
        return facultyService.getStudentsByFacultyId(id);
    }
}
