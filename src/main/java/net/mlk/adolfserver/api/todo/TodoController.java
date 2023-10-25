package net.mlk.adolfserver.api.todo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.mlk.adolfserver.AdolfServerApplication;
import net.mlk.adolfserver.data.session.Session;
import net.mlk.adolfserver.data.todo.TodoElement;
import net.mlk.adolfserver.data.todo.TodoRepository;
import net.mlk.adolfserver.data.todo.TodoService;
import net.mlk.adolfserver.errors.ResponseError;
import net.mlk.jmson.JsonList;
import net.mlk.jmson.utils.JsonConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
@ControllerAdvice
public class TodoController {
    public static final int MAX_FILE_SIZE = 10; // mb

    @PostMapping(path = {"/todo", "/todo/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> todoCreate(@RequestParam(value = "header") String header,
                                             @RequestParam(value = "task_time") String taskTime,
                                             @RequestParam(value = "description", required = false) String description,
                                             @RequestParam(value = "files", required = false) MultipartFile[] files,
                                             HttpServletRequest request,
                                             HttpServletResponse response) throws IOException {
        Session session = (Session) request.getAttribute("session");
        String name = session.getName();

        if (header.isEmpty()) {
            return new ResponseEntity<>(new ResponseError("Заголовок пустует...").toString(), HttpStatus.BAD_REQUEST);
        } else if (taskTime != null && !compareFormat(taskTime, AdolfServerApplication.FORMAT)) {
            return new ResponseEntity<>(new ResponseError("Неверный формат времени.").toString(), HttpStatus.BAD_REQUEST);
        } else if (files != null) {
            if (files.length > 5) {
                return new ResponseEntity<>(new ResponseError("Максимум можно загрузить 5 файлов.").toString(), HttpStatus.BAD_REQUEST);
            }
        }

        JsonList fileNames = new JsonList();

        if (files != null) {
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();
                fileNames.add(fileName);
                try (OutputStream outStream = new FileOutputStream("userFiles/" + name + "/" + fileName)) {
                    outStream.write(file.getBytes());
                }
            }
        }

        LocalDateTime created = LocalDateTime.now();
        LocalDateTime tasked = null;
        if (taskTime != null) {
            tasked = LocalDateTime.parse(taskTime, AdolfServerApplication.FORMAT);
        }
        TodoElement element = new TodoElement(name, header, description, fileNames, created, tasked);
        return new ResponseEntity<>(JsonConverter.convertToJson(element).toString(), HttpStatus.OK);
    }

    @GetMapping(path = {"/todo", "/todo/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> todoGet(@RequestParam(value = "date", required = false) String date,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
        TodoRepository todoRepository = TodoService.getTodoRepository();
        Session session = (Session) request.getAttribute("session");
        String name = session.getName();
        JsonList elements;

        if (date != null) {
            if (!compareFormat(date, AdolfServerApplication.FORMAT)) {
                return new ResponseEntity<>(new ResponseError("Неверный формат времени.").toString(), HttpStatus.BAD_REQUEST);
            }
            elements = todoRepository.findAllByNameIgnoreCaseAndTaskTime(name, LocalDateTime.parse(date, AdolfServerApplication.FORMAT));
        } else {
            elements = todoRepository.findAllDatesByNameIgnoreCase(name).parseTypes(false);
        }

        return new ResponseEntity<>(elements.toString(), HttpStatus.OK);
    }

    @DeleteMapping(path = {"/todo", "/todo/"})
    public ResponseEntity<String> todoDelete(@RequestParam(value = "id") int id,
                                             HttpServletRequest request,
                                             HttpServletResponse response) {
        Session session = (Session) request.getAttribute("session");
        String name = session.getName();
        TodoRepository todoRepository = TodoService.getTodoRepository();

        TodoElement element = todoRepository.findByIdAndNameIgnoreCase(id, name);
        if (element == null) {
            return new ResponseEntity<>(new ResponseError("Записи не существует.").toString(), HttpStatus.BAD_REQUEST);
        }
        todoRepository.delete(element);
        return new ResponseEntity<>(JsonConverter.convertToJson(element).toString(), HttpStatus.OK);
    }

    private static boolean compareFormat(String inputValue, DateTimeFormatter format) {
        try {
            format.parse(inputValue);
            return true;
        } catch (DateTimeParseException dtpe) {
            return false;
        }
    }


    @ExceptionHandler(MultipartException.class)
    @ResponseBody
    public ResponseEntity<String> multipartExceptionHandler(MultipartException e) {
        return new ResponseEntity<>(new ResponseError("Размер файлов не должен быть больше " + MAX_FILE_SIZE + "мб").toString(), HttpStatus.BAD_REQUEST);
    }

}
