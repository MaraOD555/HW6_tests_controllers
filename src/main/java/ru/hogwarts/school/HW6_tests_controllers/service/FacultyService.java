package ru.hogwarts.school.HW6_tests_controllers.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.HW6_tests_controllers.model.Faculty;
import ru.hogwarts.school.HW6_tests_controllers.repository.FacultyRepository;

import java.util.Collection;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }
    public Faculty findFaculty(long id) {
        return facultyRepository.findById(id).orElse(null);
    }
    public Faculty editFaculty(Faculty faculty) {
       return facultyRepository.save(faculty);
    }

    public void deleteFaculty(long id){
        facultyRepository.deleteById(id);
    }
    public Collection<Faculty> findFacultyByColor(String color) {
        return facultyRepository.findByColor(color);
    }
    public Collection<Faculty> findByColorOrNameIgnoreCase(String name, String color) {
        return facultyRepository.findByColorOrNameIgnoreCase(name, color);
    }
}
