package ru.hogwarts.school.HW6_tests_controllers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.HW6_tests_controllers.model.Faculty;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    List<Faculty> findByColor(String color);
}
