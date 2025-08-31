package ru.hogwarts.school_test.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school_test.model.Avatar;
import ru.hogwarts.school_test.model.Faculty;
import ru.hogwarts.school_test.model.Student;
import ru.hogwarts.school_test.service.AvatarService;
import ru.hogwarts.school_test.service.StudentService;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.springframework.http.HttpHeaders;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

@RestController
@RequestMapping ("/api/student")
public class StudentController {
    @Autowired
    private final StudentService studentService;
    private final AvatarService avatarService;
    private long studentId;

    public StudentController(StudentService studentService, AvatarService avatarService) {

        this.studentService = studentService;
        this.avatarService = avatarService;
    }

    @GetMapping ("/{id}") // GET http://localhost:8080/student/id
    public ResponseEntity<Student> getStudentInfo(@PathVariable long id) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PostMapping //POST http://localhost:8080/student
    public Student createStudent (@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        Student updateStudent = studentService.updateStudent(student);
        if (updateStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(updateStudent);
    }

    @DeleteMapping ("/{studentID}") // DELETE http://localhost:8080/student/id
    public ResponseEntity deleteStudent(@PathVariable long id) {
      
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    // Загрузка аватара

    @PostMapping(value = "/{studentID}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) throws IOException {
        if (avatar.getSize()>= 1024 * 300) {
            return ResponseEntity.badRequest().body("File is too big");
        }
        avatarService.uploadAvatar(id, avatar);
        return ResponseEntity.ok().build();
    }

    // Получение аватара из БД)
    @GetMapping(value = "/{studentID}/avatar/preview")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long studentID) {
        Avatar avatar = avatarService.findAvatar(studentId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getPreview().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getPreview());
    }

    // Чтение файла с локального диска
    @GetMapping(value = "/{id}/avatar-from-file")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarService.findAvatar(id);
        Path path = Path.of(avatar.getFilePath());
        try(InputStream is = Files.newInputStream(path);
            OutputStream os = response.getOutputStream();) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);
        }
    }
    // Получение студентов по диапазону возраста
    @GetMapping("/age")
    public Collection<Student> getStudentsByAgeBetween(
            @RequestParam("min") int min,
            @RequestParam("max") int max) {
        return studentService.getStudentsByAgeBetween(min, max);
    }

    // Получить факультет студента
    @GetMapping("/{id}/faculty")
    public Faculty getFacultyByStudent(@PathVariable Long id) {
        return studentService.getFacultyByStudentId(id);
    }
    // Получить количество всех студентов в школе
    @GetMapping("/count")
    public int getCountOfAllStudents() {
        return studentService.getCountOfAllStudents();
    }

    // Получить средний возраст студентов
    @GetMapping("/average-age")
    public double getAverageAgeOfStudents() {
        return studentService.getAverageAgeOfStudents();
    }

    // Получить пять последних студентов
    @GetMapping("/last-five")
    public Collection<Student> getLastFiveStudents() {
        return studentService.getLastFiveStudents();
    }
}

