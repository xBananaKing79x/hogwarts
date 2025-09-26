package ru.hogwarts.school_test.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school_test.model.Faculty;
import ru.hogwarts.school_test.model.Student;
import ru.hogwarts.school_test.repositories.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
public class StudentService {

    private StudentRepository studentRepository;
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // создание студента
    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        try {
            Student createdStudent = studentRepository.save(student);
            logger.debug("Successfully created student with id: {}", createdStudent.getId());
            return createdStudent;
        } catch (Exception e) {
            logger.error("Error occurred while creating student: {}", e.getMessage());
            throw e;
        }
    }

    // получение студента по ID
    public Student getStudentById(long id) {
        logger.info("Was invoked method for get student by id: {}", id);
        try {
            Student student = studentRepository.findById(id).orElse(null);
            if (student == null) {
                logger.warn("Student with id {} not found", id);
            } else {
                logger.debug("Successfully found student with id: {}", id);
            }
            return student;
        } catch (Exception e) {
            logger.error("Error occurred while getting student by id {}: {}", id, e.getMessage());
            throw e;
        }
    }

    // получение всех студентов
    public Collection<Student> getAllStudents() {
        logger.info("Was invoked method for get all students");
        try {
            Collection<Student> students = studentRepository.findAll();
            logger.debug("Successfully retrieved {} students", students.size());
            return students;
        } catch (Exception e) {
            logger.error("Error occurred while getting all students: {}", e.getMessage());
            throw e;
        }
    }

    // обновление студента
    public Student updateStudent(Student student) {
        logger.info("Was invoked method for update student with id: {}", student.getId());
        try {
            if (studentRepository.existsById(student.getId())) {
                Student updatedStudent = studentRepository.save(student);
                logger.debug("Successfully updated student with id: {}", student.getId());
                return updatedStudent;
            } else {
                logger.warn("Attempt to update non-existent student with id: {}", student.getId());
                return null;
            }
        } catch (Exception e) {
            logger.error("Error occurred while updating student with id {}: {}", student.getId(), e.getMessage());
            throw e;
        }
    }

    // удаление студента
    public void deleteStudent(long id) {
        logger.info("Was invoked method for delete student with id: {}", id);
        try {
            if (studentRepository.existsById(id)) {
                studentRepository.deleteById(id);
                logger.debug("Successfully deleted student with id: {}", id);
            } else {
                logger.warn("Attempt to delete non-existent student with id: {}", id);
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting student with id {}: {}", id, e.getMessage());
            throw e;
        }
    }

    // Получение студентов по диапазону возраста
    public Collection<Student> getStudentsByAgeBetween(int min, int max) {
        logger.info("Was invoked method for get students by age between {} and {}", min, max);
        try {
            Collection<Student> students = studentRepository.findByAgeBetween(min, max);
            logger.debug("Successfully found {} students with age between {} and {}", students.size(), min, max);
            return students;
        } catch (Exception e) {
            logger.error("Error occurred while getting students by age between {} and {}: {}", min, max, e.getMessage());
            throw e;
        }
    }

    // Получить факультет студента
    public Faculty getFacultyByStudentId(Long studentId) {
        try {
            Student student = studentRepository.findById(studentId).orElse(null);
            if (student == null) {
                logger.warn("Student with id {} not found when trying to get faculty", studentId);
                return null;
            }
            Faculty faculty = student.getFaculty();
            if (faculty == null) {
                logger.debug("Student with id {} has no assigned faculty", studentId);
            } else {
                logger.debug("Successfully found faculty {} for student with id {}", faculty.getId(), studentId);
            }
            return faculty;
        } catch (Exception e) {
            logger.error("Error occurred while getting faculty by student id {}: {}", studentId, e.getMessage());
            throw e;
        }
    }

    // Получить количество всех студентов в школе
    public int getCountOfAllStudents() {
        logger.info("Was invoked method for get count of all students");
        try {
            int count = studentRepository.getCountOfAllStudents();
            logger.debug("Successfully retrieved count of all students: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("Error occurred while getting count of all students: {}", e.getMessage());
            throw e;
        }
    }

    // Получить средний возраст студентов
    public double getAverageAgeOfStudents() {
        logger.info("Was invoked method for get average age of students");
        try {
            double averageAge = studentRepository.getAverageAgeOfStudents();
            logger.debug("Successfully calculated average age of students: {}", averageAge);
            return averageAge;
        } catch (Exception e) {
            logger.error("Error occurred while getting average age of students: {}", e.getMessage());
            throw e;
        }
    }

    // Получить пять последних студентов
    public Collection<Student> getLastFiveStudents() {
        logger.info("Was invoked method for get last five students");
        try {
            Collection<Student> students = studentRepository.getLastFiveStudents();
            logger.debug("Successfully retrieved last {} students", students.size());
            return students;
        } catch (Exception e) {
            logger.error("Error occurred while getting last five students: {}", e.getMessage());
            throw e;
        }
    }

    // Получение всех имен студентов, чье имя начинается с буквы А, отсортированных в алфавитном порядке в верхнем регистре
    public List<String> getStudentNamesStartingWithA() {
        logger.info("Was invoked method for get student names starting with A");
        try {
            List<String> names = studentRepository.findAll().stream()
                    .map(Student::getName)
                    .filter(name -> name != null && !name.isEmpty() && name.toUpperCase().startsWith("A"))
                    .map(String::toUpperCase)
                    .sorted()
                    .collect(toList());

            logger.debug("Successfully found {} student names starting with A", names.size());
            return names;
        } catch (Exception e) {
            logger.error("Error occurred while getting student names starting with A: {}", e.getMessage());
            throw e;
        }
    }
    // Получение среднего возраста всех студентов
    public double getAverageAgeOfAllStudents() {
        logger.info("Was invoked method for get average age of all students");
        try {
            List<Student> students = studentRepository.findAll();
            if (students.isEmpty()) {
                logger.warn("No students found when calculating average age");
                return 0.0;
            }
            double averageAge = students.stream()
                    .mapToInt(Student::getAge)
                    .average()
                    .orElse(0.0);

            logger.debug("Successfully calculated average age: {}", averageAge);
            return averageAge;
        } catch (Exception e) {
            logger.error("Error occurred while getting average age of all students: {}", e.getMessage());
            throw e;
        }
    }
    public void printStudentsInParallel() {
        logger.info("Was invoked method for printing students in parallel Threads");

        List<String> studentNames = studentRepository.findAll().stream()
                .map(Student::getName)
                .filter(name -> name != null && !name.isEmpty())
                .toList(); // Используем List вместо Collection

        if (studentNames.size() > 6) {
            logger.warn("More than 6 students found, only 6 will be printed", studentNames.size());
        }

        // Выводим первые два имени в основном потоке
        if (studentNames.size() > 0) {
            System.out.println("Main Thread - " + studentNames.get(0));
        }
        if (studentNames.size() > 1) {
            System.out.println("Main Thread - " + studentNames.get(1));
        }

        // Создаем пул потоков для параллельной обработки
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Выводим имена 3 и 4 в параллельном потоке
        if (studentNames.size() > 2) {
            executor.submit(() -> {
                System.out.println(Thread.currentThread().getName() + " - " + studentNames.get(2));
                if (studentNames.size() > 3) {
                    System.out.println(Thread.currentThread().getName() + " - " + studentNames.get(3));
                }
            });
        }
        // Выводим имена 5 и 6 в еще одном параллельном потоке
        if (studentNames.size() > 4) {
            executor.submit(() -> {
                System.out.println(Thread.currentThread().getName() + " - " + studentNames.get(4));
                if (studentNames.size() > 5) {
                    System.out.println(Thread.currentThread().getName() + " - " + studentNames.get(5));
                }
            });
        }

        // Завершаем выполнение пула потоков
        executor.shutdown();

        logger.debug("Successfully printed students in parallel mode");
    }
}

