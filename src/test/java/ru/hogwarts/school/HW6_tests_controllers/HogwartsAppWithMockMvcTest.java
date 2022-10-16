package ru.hogwarts.school.HW6_tests_controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.HW6_tests_controllers.controller.FacultyController;
import ru.hogwarts.school.HW6_tests_controllers.model.Faculty;
import ru.hogwarts.school.HW6_tests_controllers.repository.FacultyRepository;
import ru.hogwarts.school.HW6_tests_controllers.service.FacultyService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FacultyController.class) // указываем какой контроллер будем тестировать
public class HogwartsAppWithMockMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean // все методы пустышки
    private FacultyRepository facultyRepository;

    @SpyBean // все методы как в классе
    private FacultyService facultyService;

    @Autowired
    private ObjectMapper objectMapper;// когда хотим получить на выходе коллекцию

    @Test
    public void testFindFaculty() throws Exception{
        long id = 1L;
        String name = "Слизерин";
        String color = "Желтый"; //определили параметры факультета

        Faculty faculty = new Faculty(); // строим объектное представление
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty)); // т.к. мок на репозитории, то говорим, что хотим
        // получить. когда будет вызван метод
        mockMvc.perform(MockMvcRequestBuilders
                .get("/faculty/{id}", id) // сюда будет запрос
                .contentType(MediaType.APPLICATION_JSON) // получаем json
                .accept(MediaType.APPLICATION_JSON))
             .andExpect(status().isOk())// что ожидаем
             .andExpect(jsonPath("$.id").value(id)) //$ одначает тело ответа
             .andExpect(jsonPath("$.name").value(name))
             .andExpect(jsonPath("$.color").value(color));
    }
    @Test
    public void testFindFacultiesByColor() throws Exception{
        long id1 = 1L;
        String name1 = "Слизерин";
        long id2 = 2L;
        String name2 = "Гриффиндор";

        String color = "Желтый";

        Faculty faculty1 = new Faculty(); // строим объектное представление
        faculty1.setId(id1);
        faculty1.setName(name1);
        faculty1.setColor(color);

        Faculty faculty2 = new Faculty(); // строим объектное представление
        faculty2.setId(id2);
        faculty2.setName(name2);
        faculty2.setColor(color);

        when(facultyRepository.findByColor(color)).thenReturn(List.of(faculty1, faculty2)); // т.к. мок на репозитории, то говорим, что хотим
        // получить. когда будет вызван метод
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty") // сюда будет запрос
                        .queryParam("color", color)
                        .contentType(MediaType.APPLICATION_JSON) // получаем json
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())// что ожидаем
                .andExpect((ResultMatcher) content().json(objectMapper.writeValueAsString(List.of(faculty1, faculty2))));

    }
    @Test
    public void testFindFacultiesByColorOrNameIgnoreCase() throws Exception{
        long id1 = 1L;
        String name1 = "Слизерин";
        String color1 = "Желтый";
        String color1IgnoreCase = "желтый";
        long id2 = 2L;
        String name2 = "Гриффиндор";
        String color2 = "Красный";
        String name2IgnoreCase = "гриффиндор";

        Faculty faculty1 = new Faculty(); // строим объектное представление
        faculty1.setId(id1);
        faculty1.setName(name1);
        faculty1.setColor(color1);

        Faculty faculty2 = new Faculty(); // строим объектное представление
        faculty2.setId(id2);
        faculty2.setName(name2);
        faculty2.setColor(color2);

        when(facultyRepository.findByColorOrNameIgnoreCase(color1IgnoreCase, name2IgnoreCase)).thenReturn(Set.of(faculty1, faculty2)); // т.к. мок на репозитории, то говорим, что хотим
        // получить. когда будет вызван метод
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty") // сюда будет запрос
                        .queryParam("color", color1IgnoreCase)
                        .queryParam("name", name2IgnoreCase)
                        .contentType(MediaType.APPLICATION_JSON) // получаем json
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())// что ожидаем
                .andExpect((ResultMatcher) content().json(objectMapper.writeValueAsString(List.of(faculty1, faculty2))));

    }
    @Test
    public void testAddFaculty() throws Exception{
        long id = 1L;
        String name = "Слизерин";
        String color = "Желтый"; //определили параметры факультета

        JSONObject facultyObj = new JSONObject(); // формируется представление а json
        facultyObj.put("name", name);
        facultyObj.put("color", color);

        Faculty faculty = new Faculty(); // строим объектное представление
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty); // т.к. мок на репозитории, то говорим, что хотим
        // получить. когда будет вызван метод
        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty") // сюда будет запрос
                        .content(facultyObj.toString())
                        .contentType(MediaType.APPLICATION_JSON) // получаем json
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())// что ожидаем
                .andExpect(jsonPath("$.id").value(id)) //$ одначает тело ответа
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }
    @Test
    public void testUpdateFaculty() throws Exception{
        long id = 1L;
        String oldName = "Слизерин";
        String oldColor = "Желтый"; //определили параметры факультета

        String newName = "Пуфендуй";
        String newColor = "Зеленый";

        JSONObject facultyObj = new JSONObject(); // формируется представление а json
        facultyObj.put("id", id);
        facultyObj.put("name", newName);
        facultyObj.put("color", newColor);

        Faculty faculty = new Faculty(); // строим объектное представление
        faculty.setId(id);
        faculty.setName(oldName);
        faculty.setColor(oldColor);

        Faculty updatedFaculty = new Faculty(); // строим объектное представление
        faculty.setId(id);
        faculty.setName(newName);
        faculty.setColor(newColor);

        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));
        when(facultyRepository.save(any(Faculty.class))).thenReturn(updatedFaculty); // т.к. мок на репозитории, то говорим, что хотим
        // получить. когда будет вызван метод

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty") // сюда будет запрос
                        .content(facultyObj.toString())
                        .contentType(MediaType.APPLICATION_JSON) // получаем json
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())// что ожидаем
                .andExpect(jsonPath("$.id").value(id)) //$ одначает тело ответа
                .andExpect(jsonPath("$.name").value(newName))
                .andExpect(jsonPath("$.color").value(newColor));
    }
    @Test
    public void testDeleteFaculty() throws Exception{
        long id = 1L;
        String name = "Слизерин";
        String color = "Желтый"; //определили параметры факультета

        Faculty faculty = new Faculty(); // строим объектное представление
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/{id}", id) // сюда будет запрос
                        .contentType(MediaType.APPLICATION_JSON) // получаем json
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())// что ожидаем
                .andExpect(jsonPath("$.id").value(id)) //$ одначает тело ответа
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));

        verify(facultyRepository, atLeastOnce()).deleteById(id); // проверка, что вызывается метод репозитория
    }




}
