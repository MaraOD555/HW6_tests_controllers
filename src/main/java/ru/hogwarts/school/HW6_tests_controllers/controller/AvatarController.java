package ru.hogwarts.school.HW6_tests_controllers.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.HW6_tests_controllers.model.Avatar;
import ru.hogwarts.school.HW6_tests_controllers.service.AvatarService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/{studentId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)//адресе запроса передаем идентификатор
    // студента, приложение ожидает в запросе тип содержимого multipart/form-data
    public ResponseEntity<String> uploadAvatar(@PathVariable Long studentId, @RequestParam MultipartFile avatar) throws IOException { //Из тела запроса получаем объект класса MultipartFile
        avatarService.uploadAvatar(studentId, avatar);
        return ResponseEntity.ok().build(); // отправляем ответ пользователю, что аватар загружен в приложение
    }
    @GetMapping(value = "/{id}/avatar-from-db")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {//чтение файла из БД (обычно это превью, небольшой объекм данных)
        // (чтобы пользователь получил картинку. если запрашивает инфо)
        // важно правильно выполнить заголовки, возвращаются данные в виде массива байт, поэтому заголовки для обработки данных которые мы получили
        Avatar avatar = avatarService.findAvatar(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));// какой типа данных
        headers.setContentLength(avatar.getData().length);// длина контента, чтобы быть уверенными
        // что данные не потерялись. Браузер опеределяет сколько инфо загружено
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }
    @GetMapping(value = "/{id}/avatar-from-file") //чтение файла с локального диска, оригинального файло, который был загружен пользователем
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException{
        Avatar avatar = avatarService.findAvatar(id);
        Path path = Path.of(avatar.getFilePath());
        try(InputStream is = Files.newInputStream(path);// стримы
            OutputStream os = response.getOutputStream();) {
            response.setStatus(200); // заголовки как в случае с БД
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);
        }
    }
}
