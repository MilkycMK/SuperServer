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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
@ControllerAdvice
public class TodoController {

    @PostMapping(path = {"/todo", "/todo/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> todoCreate(@RequestParam(value = "header") String header,
                                             @RequestParam(value = "task_time") String taskTime,
                                             @RequestParam(value = "description", required = false) String description,
                                             @RequestParam(value = "files", required = false) MultipartFile[] files,
                                             HttpServletRequest request,
                                             HttpServletResponse response) {
        Session session = (Session) request.getAttribute("session");
        String name = session.getUserName();

        if (header == null || header.isEmpty()) {
            return new ResponseEntity<>(new ResponseError("Заголовок не может быть пустым.").toString(), HttpStatus.OK);
        } else if (taskTime == null || !compareTimeDateFormat(taskTime)) {
            return new ResponseEntity<>(new ResponseError("Неверный формат времени.").toString(), HttpStatus.BAD_REQUEST);
        } else if (files != null) {
            if (files.length > 5) {
                return new ResponseEntity<>(new ResponseError("Максимум можно загрузить 5 файлов.").toString(), HttpStatus.BAD_REQUEST);
            }
        }

        LocalDateTime created = LocalDateTime.now();
        LocalDateTime tasked = LocalDateTime.parse(taskTime, AdolfServerApplication.TIMEDATE_FORMAT);
        TodoElement element = new TodoElement(name, header, description, files, created, tasked);
        return new ResponseEntity<>(JsonConverter.convertToJson(element).toString(), HttpStatus.OK);
    }

    @GetMapping(path = {"/todo", "/todo/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> todoGet(@RequestParam(value = "date", required = false) String date,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
        Session session = (Session) request.getAttribute("session");
        String name = session.getUserName();
        TodoRepository todoRepository = TodoService.getTodoRepository();
        JsonList elements = new JsonList();

        if (date == null) {
            elements = todoRepository.findAllDatesByName(name).parseTypes(false);
        } else {
            if (!compareDateFormat(date)) {
                return new ResponseEntity<>(new ResponseError("Неверный формат даты.").toString(), HttpStatus.BAD_REQUEST);
            }
            List<TodoElement> raw = todoRepository.findAllByUserNameAndTaskTime(name, LocalDate.parse(date));
            for (TodoElement element : raw) {
                elements.append(JsonConverter.convertToJson(element));
            }
        }

        return new ResponseEntity<>(elements.toString(), HttpStatus.OK);
    }

    @DeleteMapping(path = {"/todo", "/todo/"})
    public ResponseEntity<String> todoDelete(@RequestParam(value = "id") int id,
                                             HttpServletRequest request,
                                             HttpServletResponse response) {
        TodoRepository todoRepository = TodoService.getTodoRepository();
        Session session = (Session) request.getAttribute("session");
        String name = session.getUserName();

        TodoElement element = todoRepository.findByIdAndUserName(id, name);
        if (element == null) {
            return new ResponseEntity<>(new ResponseError("Записи не существует.").toString(), HttpStatus.BAD_REQUEST);
        }
        element.deleteFiles();
        todoRepository.delete(element);
        return new ResponseEntity<>(JsonConverter.convertToJson(element).toString(), HttpStatus.OK);
    }

    private static boolean compareTimeDateFormat(String inputValue) {
        try {
            AdolfServerApplication.TIMEDATE_FORMAT.parse(inputValue);
            return true;
        } catch (DateTimeParseException dtpe) {
            return false;
        }
    }

    private static boolean compareDateFormat(String inputValue) {
        try {
            AdolfServerApplication.DATE_FORMAT.parse(inputValue);
            return true;
        } catch (DateTimeParseException dtpe) {
            return false;
        }
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<String> multipartExceptionHandler(MultipartException ex) {
        return new ResponseEntity<>(new ResponseError("Размер файлов не должен быть больше "
                + AdolfServerApplication.MAX_FILE_SIZE + "мб").toString(), HttpStatus.BAD_REQUEST);
    }

}
