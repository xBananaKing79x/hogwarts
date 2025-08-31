package ru.hogwarts.school_test.model;

import jakarta.persistence.*;

@Entity
public class Student {
    @Id
    @GeneratedValue
    private long id;
    String name;
    int age;
    // Связь ManyToOne с Faculty
    // FetchType.LAZY означает, что факультет будет загружаться только при необходимости
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id") // имя столбца в таблице student, которое будет содержать foreign key
    private Faculty faculty;

    public Student(long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }


    // Геттеры
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
    public Faculty getFaculty() {
        return faculty;
    }

    // Сеттеры
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }
}
