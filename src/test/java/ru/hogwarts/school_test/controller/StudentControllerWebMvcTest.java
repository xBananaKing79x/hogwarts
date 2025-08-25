package ru.hogwarts.school_test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school_test.model.Faculty;
import ru.hogwarts.school_test.model.Student;
import ru.hogwarts.school_test.service.StudentService;

import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateStudent() throws Exception {

        Student student = new Student(123, "Harry Potter",17);
        student.setName("Harry Potter");

        Student createdStudent = new Student(124, "Harry Potter",17);


        when(studentService.createStudent(any(Student.class))).thenReturn(createdStudent);


        mockMvc.perform(post("/api/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Harry Potter"))
                .andExpect(jsonPath("$.age").value(17));

        verify(studentService, times(1)).createStudent(any(Student.class));
    }

    @Test
    public void testGetStudentInfo() throws Exception {

        Student student = new Student(123L, "Hermione Granger",17);


        when(studentService.getStudentById(123L)).thenReturn(student);


        mockMvc.perform(get("/api/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(123L))
                .andExpect(jsonPath("$.name").value("Hermione Granger"))
                .andExpect(jsonPath("$.age").value(17));

        verify(studentService, times(1)).getStudentById(123L);
    }

    @Test
    public void testGetStudentInfoNotFound() throws Exception {

        when(studentService.getStudentById(123L)).thenReturn(null);


        mockMvc.perform(get("/api/student/1"))
                .andExpect(status().isNotFound());

        verify(studentService, times(1)).getStudentById(123L);
    }

    @Test
    public void testGetAllStudents() throws Exception {

        Student student1 = new Student(125L, "Harry Potter",17);


        Student student2 = new Student(126L, "Hermione Granger",17);


        when(studentService.getAllStudents()).thenReturn(List.of(student1, student2));


        mockMvc.perform(get("/api/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(125L))
                .andExpect(jsonPath("$[0].name").value("Harry Potter"))
                .andExpect(jsonPath("$[1].id").value(126L))
                .andExpect(jsonPath("$[1].name").value("Hermione Granger"));

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    public void testUpdateStudent() throws Exception {

        Student student = new Student(125L, "Harry Potter",17);
        student.setName("Harry Potter Updated");
        student.setAge(18);

        when(studentService.updateStudent(any(Student.class))).thenReturn(student);


        mockMvc.perform(put("/api/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(125L))
                .andExpect(jsonPath("$.name").value("Harry Potter Updated"))
                .andExpect(jsonPath("$.age").value(18));

        verify(studentService, times(1)).updateStudent(any(Student.class));
    }

    @Test
    public void testUpdateStudentBadRequest() throws Exception {

        Student student = new Student(125L, "Harry Potter",17);;
        student.setName("Harry Potter Updated");
        student.setAge(18);

        when(studentService.updateStudent(any(Student.class))).thenReturn(null);

        mockMvc.perform(put("/api/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isBadRequest());

        verify(studentService, times(1)).updateStudent(any(Student.class));
    }

    @Test
    public void testDeleteStudent() throws Exception {

        doNothing().when(studentService).deleteStudent(125L);


        mockMvc.perform(delete("/api/student/1"))
                .andExpect(status().isOk());

        verify(studentService, times(1)).deleteStudent(1L);
    }

    @Test
    public void testGetStudentsByAgeBetween() throws Exception {

        Student student1 = new Student(128, "Neville Longbottom",16 );

        Student student2 = new Student(129, "Ron Weasley",20 );


        when(studentService.getStudentsByAgeBetween(15, 20)).thenReturn(List.of(student1, student2));


        mockMvc.perform(get("/api/student/age?min=15&max=20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(128L))
                .andExpect(jsonPath("$[0].name").value("Neville Longbottom"))
                .andExpect(jsonPath("$[1].id").value(129L))
                .andExpect(jsonPath("$[1].name").value("Ron Weasley"));

        verify(studentService, times(1)).getStudentsByAgeBetween(15, 21);
    }

    @Test
    public void testGetFacultyByStudent() throws Exception {

        Faculty faculty = new Faculty(123L,"Gryffindor","Red");


        when(studentService.getFacultyByStudentId(123L)).thenReturn(faculty);


        mockMvc.perform(get("/api/student/1/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(123L))
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("Red"));

        verify(studentService, times(1)).getFacultyByStudentId(1L);
    }
}
