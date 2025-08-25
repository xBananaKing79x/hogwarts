package ru.hogwarts.school_test.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import ru.hogwarts.school_test.model.Faculty;
import ru.hogwarts.school_test.model.Student;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateFaculty() {
        // Создаем факультет для проверки
        Faculty faculty = new Faculty(124L,"Gryffindor","Red");


        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/faculty", faculty, Faculty.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Gryffindor");
        assertThat(response.getBody().getColor()).isEqualTo("Red");
    }

    @Test
    public void testGetFaculty() {
        // Создаем факультет для проверки
        Faculty faculty = new Faculty(125L,"Slytherin","Green");
        Faculty createdFaculty = restTemplate.postForObject(
                "http://localhost:" + port + "/faculty", faculty, Faculty.class);
        Long facultyId = createdFaculty.getId();


        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/" + facultyId, Faculty.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(facultyId);
        assertThat(response.getBody().getName()).isEqualTo("Slytherin");
    }

    @Test
    public void testGetAllFaculties() {
        // Создаем факультет для проверки
        Faculty faculty = new Faculty(125L,"Hufflepuff","Yellow");
        restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Faculty.class);


        ResponseEntity<Collection<Faculty>> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Faculty>>() {});


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    public void testUpdateFaculty() {
        // Создаем факультет для проверки
        Faculty faculty = new Faculty(126L,"Ravenclaw","Blue");
        Faculty createdFaculty = restTemplate.postForObject(
                "http://localhost:" + port + "/faculty", faculty, Faculty.class);
        createdFaculty.setName("Ravenclaw Updated");
        createdFaculty.setColor("Silver");


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Faculty> requestEntity = new HttpEntity<>(createdFaculty, headers);

        ResponseEntity<Faculty> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty",
                HttpMethod.PUT,
                requestEntity,
                Faculty.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Ravenclaw Updated");
        assertThat(response.getBody().getColor()).isEqualTo("Silver");
    }

    @Test
    public void testDeleteFaculty() {
        // Создаем факультет для проверки
        Faculty faculty = new Faculty(125L,"Deleted Faculty","Black");

        Faculty createdFaculty = restTemplate.postForObject(
                "http://localhost:" + port + "/faculty", faculty, Faculty.class);
        Long facultyId = createdFaculty.getId();


        restTemplate.delete("http://localhost:" + port + "/faculty/" + facultyId);


        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/" + facultyId, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGetFacultiesByColor() {
        // Создаем факультет для проверки
        Faculty faculty = new Faculty(126L,"Test Faculty","Purple");
        restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Faculty.class);


        ResponseEntity<Collection<Faculty>> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/color/Purple",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Faculty>>() {});


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
        // Проверяем, что факультет имеет правильный цвет
        response.getBody().forEach(f -> assertThat(f.getColor()).isEqualTo("Purple"));
    }

    @Test
    public void testGetFacultiesByNameOrColorIgnoreCase() {
        // Создаем факультет для проверки
        Faculty faculty = new Faculty(127L,"SEARCH TEST","Orange");
        restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, Faculty.class);


        ResponseEntity<Collection<Faculty>> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/search?query=search test",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Faculty>>() {});


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void testGetStudentsByFaculty() {
        // Создаем факультет для проверки
        Faculty faculty = new Faculty(128L,"Test Faculty for Students","Pink");

        Faculty createdFaculty = restTemplate.postForObject(
                "http://localhost:" + port + "/faculty", faculty, Faculty.class);
        Long facultyId = createdFaculty.getId();


        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/" + facultyId + "/students",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {});


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
}
