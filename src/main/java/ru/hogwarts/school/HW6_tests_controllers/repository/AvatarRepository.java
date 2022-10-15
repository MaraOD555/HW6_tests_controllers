package ru.hogwarts.school.HW6_tests_controllers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.HW6_tests_controllers.model.Avatar;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
     Optional<Avatar> findAvatarByStudentId (long studentId);
}
