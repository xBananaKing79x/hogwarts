package ru.hogwarts.school_test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school_test.model.Faculty;
import ru.hogwarts.school_test.model.Student;
import ru.hogwarts.school_test.repositories.FacultyRepository;
import ru.hogwarts.school_test.repositories.StudentRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    // создание факультета
    public Faculty createFaculty(Faculty faculty) {

        logger.info("Was invoked method for create faculty");
        try {
            Faculty createdFaculty = (Faculty) facultyRepository.save(faculty);
            logger.debug("Successfully created faculty with id: {}", createdFaculty.getId());
            return createdFaculty;
        } catch (Exception e) {
            logger.error("Error occurred while creating faculty: {}", e.getMessage());
            throw e;
        }
    }

    // получение факультета по ID
    public Faculty getFacultyById(long id) {

        logger.info("Was invoked method for get faculty by id: {}", id);
        try {
            Faculty faculty = (Faculty) facultyRepository.findById(id).orElse(null);
            if (faculty == null) {
                logger.warn("Faculty with id {} not found", id);
            } else {
                logger.debug("Successfully found faculty with id: {}", id);
            }
            return faculty;
        } catch (Exception e) {
            logger.error("Error occurred while getting faculty by id {}: {}", id, e.getMessage());
            throw e;
        }
    }

    // получение всех факультетов
    public Collection<Faculty> getAllFaculties() {

        logger.info("Was invoked method for get all faculties");
        try {
            Collection<Faculty> faculties = facultyRepository.findAll();
            logger.debug("Successfully retrieved {} faculties", faculties.size());
            return faculties;
        } catch (Exception e) {
            logger.error("Error occurred while getting all faculties: {}", e.getMessage());
            throw e;
        }
    }

    // обновление факультета
    public Faculty updateFaculty(Faculty faculty) {

        logger.info("Was invoked method for update faculty with id: {}", faculty.getId());
        try {
            if (facultyRepository.existsById(faculty.getId())) {
                Faculty updatedFaculty = (Faculty) facultyRepository.save(faculty);
                logger.debug("Successfully updated faculty with id: {}", faculty.getId());
                return updatedFaculty;
            } else {
                logger.warn("Attempt to update non-existent faculty with id: {}", faculty.getId());
                return null;
            }
        } catch (Exception e) {
            logger.error("Error occurred while updating faculty with id {}: {}", faculty.getId(), e.getMessage());
            throw e;
        }
    }

    // удаление факультета
    public void deleteFaculty(long id) {
        logger.info("Was invoked method for delete faculty with id: {}", id);
        try {
            if (facultyRepository.existsById(id)) {
                facultyRepository.deleteById(id);
                logger.debug("Successfully deleted faculty with id: {}", id);
            } else {
                logger.warn("Attempt to delete non-existent faculty with id: {}", id);
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting faculty with id {}: {}", id, e.getMessage());
            throw e;
        }
    }

    // Поиск факультетов по имени или цвету, игнорируя регистр
    public Collection<Faculty> getFacultiesByNameOrColorIgnoreCase(String nameOrColor) {
        logger.info("Was invoked method for get faculties by name or color ignore case: {}", nameOrColor);
        try {
            Collection<Faculty> faculties = facultyRepository.findByNameOrColorIgnoreCase(nameOrColor, nameOrColor);
            logger.debug("Successfully found {} faculties by name or color ignore case: {}", faculties.size(), nameOrColor);
            return faculties;
        } catch (Exception e) {
            logger.error("Error occurred while getting faculties by name or color ignore case {}: {}", nameOrColor, e.getMessage());
            throw e;
        }
    }

    // Получить студентов факультета
    public Collection<Student> getStudentsByFacultyId(Long facultyId) {
        logger.info("Was invoked method for get students by faculty id: {}", facultyId);
        try {
            if (!facultyRepository.existsById(facultyId)) {
                logger.warn("Faculty with id {} not found when trying to get students", facultyId);
                return Collections.emptyList();
            }
            Collection<Student> students = studentRepository.findByFacultyId(facultyId);
            logger.debug("Successfully found {} students for faculty with id {}", students.size(), facultyId);
            return students;
        } catch (Exception e) {
            logger.error("Error occurred while getting students by faculty id {}: {}", facultyId, e.getMessage());
            throw e;
        }
    }
}

