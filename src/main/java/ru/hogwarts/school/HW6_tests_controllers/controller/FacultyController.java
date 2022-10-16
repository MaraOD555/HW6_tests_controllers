package ru.hogwarts.school.HW6_tests_controllers.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.HW6_tests_controllers.model.Faculty;
import ru.hogwarts.school.HW6_tests_controllers.service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }
    @GetMapping("{id}")
    public ResponseEntity<Faculty> findFacultyInfo (@PathVariable long id){
        Faculty faculty = facultyService.findFaculty(id);
        if(faculty == null){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }
    @PostMapping
    public Faculty createFaculty (@RequestBody Faculty faculty){
        return facultyService.createFaculty(faculty);
    }
    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty){
        Faculty facultyForEdit = facultyService.editFaculty(faculty);
        if(facultyForEdit == null){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(facultyForEdit);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable long id){
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping
    public ResponseEntity<Collection<Faculty>> findFacultyByColor (@RequestParam (required = false) String color){ // задаем цвет и ицем все факультеты по этому цвету
        if(color != null && !color.isBlank()){
            return ResponseEntity.ok(facultyService.findFacultyByColor(color));
        }
        return ResponseEntity.notFound().build();// не найдено
    }
    @GetMapping
    public ResponseEntity<Collection<Faculty>> findByNameOrColorIgnoreCase(@RequestParam (required = false) String name,
                                                                           @RequestParam (required = false) String color){
        if(name != null && !name.isBlank() && color != null && !color.isBlank()) {
            return ResponseEntity.ok(facultyService.findByColorOrNameIgnoreCase(name, color));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
