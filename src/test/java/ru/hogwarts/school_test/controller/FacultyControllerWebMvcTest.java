package ru.hogwarts.school_test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school_test.model.Faculty;
import ru.hogwarts.school_test.model.Student;
import ru.hogwarts.school_test.service.FacultyService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;


    private FacultyService facultyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateFaculty() throws Exception {

        Faculty createdFaculty = new Faculty(124L, "Gryffindor", "Red");
        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(createdFaculty);
        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdFaculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(124L))
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("Red"));

        verify(facultyService, times(1)).createFaculty(any(Faculty.class));
    }

    @Test
    public void testGetFaculty() throws Exception {

        Faculty createdfaculty = new Faculty(125L, "Slytherin", "Green");

        when(facultyService.getFacultyById(125L)).thenReturn(createdfaculty);

        mockMvc.perform(get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(125L))
                .andExpect(jsonPath("$.name").value("Slytherin"))
                .andExpect(jsonPath("$.color").value("Green"));

        verify(facultyService, times(1)).getFacultyById(125L);
    }

    @Test
    public void testGetFacultyNotFound() throws Exception {
        // Given
        when(facultyService.getFacultyById(125L)).thenReturn(null);


        mockMvc.perform(get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(facultyService, times(1)).getFacultyById(125L);
    }

    @Test
    public void testGetAllFaculties() throws Exception {
        // Given
        Faculty faculty1 = new Faculty(124L, "Gryffindor", "Red");

        Faculty faculty2 = new Faculty(125L, "Slytherin", "Green");


        when(facultyService.getAllFaculties()).thenReturn(List.of(faculty1, faculty2));


        mockMvc.perform(get("/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(124L))
                .andExpect(jsonPath("$[0].name").value("Gryffindor"))
                .andExpect(jsonPath("$[1].id").value(125L))
                .andExpect(jsonPath("$[1].name").value("Slytherin"));

        verify(facultyService, times(1)).getAllFaculties();
    }

    @Test
    public void testUpdateFaculty() throws Exception {
        // Given
        Faculty faculty = new Faculty(124L,"Gryffindor","Red");
        faculty.setId(124L);
        faculty.setName("Gryffindor Updated");
        faculty.setColor("Gold");

        when(facultyService.updateFaculty(any(Faculty.class))).thenReturn(faculty);

        // When & Then
        mockMvc.perform(put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Gryffindor Updated"))
                .andExpect(jsonPath("$.color").value("Gold"));

        verify(facultyService, times(1)).updateFaculty(any(Faculty.class));
    }

    @Test
    public void testUpdateFacultyNotFound() throws Exception {
        // Given
        Faculty faculty = new Faculty(124L,"Gryffindor","Red");
        faculty.setId(124L);
        faculty.setName("Gryffindor Updated");
        faculty.setColor("Gold");

        when(facultyService.updateFaculty(any(Faculty.class))).thenReturn(null);

        // When & Then
        mockMvc.perform(put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(facultyService, times(1)).updateFaculty(any(Faculty.class));
    }

    @Test
    public void testDeleteFaculty() throws Exception {
        // Given
        doNothing().when(facultyService).deleteFaculty(124L);

        // When & Then
        mockMvc.perform(delete("/faculty/1"))
                .andExpect(status().isOk());

        verify(facultyService, times(1)).deleteFaculty(124L);
    }

    @Test
    public void testGetFacultiesByColor() throws Exception {
        // Given
        Faculty faculty1 = new Faculty(128L,"Gryffindor","Red");


        Faculty faculty2 = new Faculty(129L,"Gryffindor","Red");


        when(facultyService.getFacultiesByColor("Red")).thenReturn(List.of(faculty1, faculty2));

        // When & Then
        mockMvc.perform(get("/faculty/color/Purple"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(128L))
                .andExpect(jsonPath("$[0].name").value("Faculty 1"))
                .andExpect(jsonPath("$[0].color").value("Red"))
                .andExpect(jsonPath("$[1].id").value(129L))
                .andExpect(jsonPath("$[1].name").value("Faculty 2"))
                .andExpect(jsonPath("$[1].color").value("Red"));

        verify(facultyService, times(1)).getFacultiesByColor("Red");
    }

    @Test
    public void testGetFacultiesByNameOrColorIgnoreCase() throws Exception {
        // Given
        Faculty faculty = new Faculty(124L,"Gryffindor","Red");


        when(facultyService.getFacultiesByNameOrColorIgnoreCase("search test")).thenReturn(List.of(faculty));

        // When & Then
        mockMvc.perform(get("/faculty/search?query=search test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(124L))
                .andExpect(jsonPath("$[0].name").value("Gryffindor"))
                .andExpect(jsonPath("$[0].color").value("Red"));

        verify(facultyService, times(1)).getFacultiesByNameOrColorIgnoreCase("Gryffindor");
    }

    @Test
    public void testGetStudentsByFaculty() throws Exception {

        Student student1 = new Student(128, "Harry Potter",17 );


        Student student2 = new Student(129, "Ron Weasley",17 );


        when(facultyService.getStudentsByFacultyId(1L)).thenReturn(List.of(student1, student2));


        mockMvc.perform(get("/faculty/1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(128L))
                .andExpect(jsonPath("$[0].name").value("Harry Potter"))
                .andExpect(jsonPath("$[1].id").value(129L))
                .andExpect(jsonPath("$[1].name").value("Ron Weasley"));

        verify(facultyService, times(1)).getStudentsByFacultyId(1L);
    }
}