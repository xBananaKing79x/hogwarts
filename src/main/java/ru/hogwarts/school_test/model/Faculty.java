package ru.hogwarts.school_test.model;

import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan
public class Faculty {
    private Long id;
    private String name;
    private String color;

    // Конструктор
    public Faculty(Long id, String name, String color) {

        this.id = id;
        this.name = name;
        this.color = color;
    }

    // Геттеры
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    // Сеттеры
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
