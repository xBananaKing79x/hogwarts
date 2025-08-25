package ru.hogwarts.school_test.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import ru.hogwarts.school_test.model.Student;
import ru.hogwarts.school_test.model.Faculty;


import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateStudent() {
        // Создаем студента для проверки
        Student student = new Student(123, "Harry Potter",17 );


        ResponseEntity<Student> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/student", student, Student.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Harry Potter");
        assertThat(response.getBody().getAge()).isEqualTo(17);
    }

    @Test
    public void testGetStudentInfo() {
        // Создаем студента для проверки
        Student student = new Student(124, "Hermione Granger",17 );
        Student createdStudent = restTemplate.postForObject(
                "http://localhost:" + port + "/api/student", student, Student.class);
        Long studentId = createdStudent.getId();


        ResponseEntity<Student> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/student/" + studentId, Student.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(studentId);
        assertThat(response.getBody().getName()).isEqualTo("Hermione Granger");
    }

    @Test
    public void testGetAllStudents() {

        // Убедимся что в БД есть запись со студентами
        Student student = new Student(125, "Ron Weasley",17 );
        restTemplate.postForObject("http://localhost:" + port + "/api/student", student, Student.class);


        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/student",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {});


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    public void testUpdateStudent() {
        // Создаем студента для проверки
        Student student = new Student(126, "Neville Longbottom",17 );
        Student createdStudent = restTemplate.postForObject(
                "http://localhost:" + port + "/api/student", student, Student.class);
        createdStudent.setName("Neville Longbottom Updated");
        createdStudent.setAge(18);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Student> requestEntity = new HttpEntity<>(createdStudent, headers);

        ResponseEntity<Student> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/student",
                HttpMethod.PUT,
                requestEntity,
                Student.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Neville Longbottom Updated");
        assertThat(response.getBody().getAge()).isEqualTo(18);
    }

    @Test
    public void testDeleteStudent() {
        // Создаем студента для проверки
        Student student = new Student(127, "Draco Malfoy",17 );
        Student createdStudent = restTemplate.postForObject(
                "http://localhost:" + port + "/api/student", student, Student.class);
        Long studentId = createdStudent.getId();

        restTemplate.delete("http://localhost:" + port + "/api/student/" + studentId);


        ResponseEntity<Student> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/student/" + studentId, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGetStudentsByAgeBetween() {
        // Создаем нескольких студентов для проверки
        Student student1 = new Student(128, "Neville Longbottom",15 );

        restTemplate.postForObject("http://localhost:" + port + "/api/student", student1, Student.class);

        Student student2 = new Student(129, "Ron Weasley",20);

        restTemplate.postForObject("http://localhost:" + port + "/api/student", student2, Student.class);


        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/student/age?min=15&max=20",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {});


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void testGetFacultyByStudent() {

        Student student = new Student(130, "Luna Lovegood",16 );

        Student createdStudent = restTemplate.postForObject(
                "http://localhost:" + port + "/api/student", student, Student.class);
        Long studentId = createdStudent.getId();
        createdStudent.setFaculty(new Faculty(123L,"Gryffindor","red"));

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/student/" + studentId + "/faculty",
                Faculty.class);


        assertThat(response.getStatusCode()).isIn(HttpStatus.OK, HttpStatus.NOT_FOUND);
    }
}