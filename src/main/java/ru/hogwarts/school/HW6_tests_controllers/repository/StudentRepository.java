package ru.hogwarts.school.HW6_tests_controllers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.HW6_tests_controllers.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> { //Spring ищет сущности по определенным полям
    List<Student> findByAge(int age);

    List<Student> findByAgeBetween(int from, int to);
}
