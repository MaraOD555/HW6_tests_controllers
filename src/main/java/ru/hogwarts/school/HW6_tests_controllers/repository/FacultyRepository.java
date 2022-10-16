package ru.hogwarts.school.HW6_tests_controllers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.HW6_tests_controllers.model.Faculty;

import java.util.List;
import java.util.Set;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    List<Faculty> findByColor(String color);
    Set<Faculty> findByColorOrNameIgnoreCase(String color, String name);
}
