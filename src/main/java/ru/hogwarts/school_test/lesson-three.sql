--liquibase formatted sql

--changeset student:1
CREATE TABLE faculty (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    color VARCHAR(100) NOT NULL
);

--changeset student:2
CREATE TABLE student (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INTEGER NOT NULL,
    faculty_id BIGINT REFERENCES faculty(id)
);

--changeset student:3
CREATE TABLE avatar (
    id BIGSERIAL PRIMARY KEY,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT NOT NULL,
    media_type VARCHAR(100) NOT NULL,
    data BYTEA,
    student_id BIGINT NOT NULL UNIQUE REFERENCES student(id)
);
--changeset student:4
-- Индекс для поиска по имени студента
CREATE INDEX idx_student_name ON student(name);

--changeset student:5
-- Индекс для поиска по названию и цвету факультета
CREATE INDEX idx_faculty_name_color ON faculty(name, color);