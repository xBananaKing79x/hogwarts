package ru.hogwarts.school_test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school_test.model.Student;
import ru.hogwarts.school_test.service.StudentService;

@RestController
@RequestMapping ("/api/student")
public class StudentController {
    @Autowired
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping ("/{id}") // GET http://localhost:8080/student/id
    public Student getStudentInfo(@PathVariable long id) {
        return studentService.getStudentById(id);
    }

    @PostMapping //POST http://localhost:8080/student
    public Student createStudent (@RequestBody Student student) {
        return studentService.createStudent(student);
    }
    @PutMapping
    public Student updateStudent (@RequestBody Student student) {
        return studentService.updateStudent(student);
    }

    @DeleteMapping ("/{id}") // DELETE http://localhost:8080/student/id
    public ResponseEntity deleteStudent(@PathVariable long id) {
         studentService.deleteStudent(id);
         return ResponseEntity.ok().build();
    }
}
