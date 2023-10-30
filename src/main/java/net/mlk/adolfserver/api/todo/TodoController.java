package net.mlk.adolfserver.api.todo;

import net.mlk.adolfserver.AdolfServerApplication;
import net.mlk.adolfserver.data.todo.TodoElement;
import net.mlk.adolfserver.data.todo.TodoService;
import net.mlk.adolfserver.data.todo.files.UserFile;
import net.mlk.adolfserver.data.todo.files.UserFilesService;
import net.mlk.adolfserver.data.user.session.Session;
import net.mlk.adolfserver.errors.ResponseError;
import net.mlk.adolfserver.utils.AdolfUtils;
import net.mlk.jmson.JsonList;
import net.mlk.jmson.utils.JsonConverter;
import net.mlk.jmson.utils.JsonConvertible;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@ControllerAdvice
public class TodoController {

    @PostMapping(path = {"/todo", "/todo/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseError> createTodo(@RequestParam String topic,
                                                    @RequestParam(name = "task_time") String taskTime,
                                                    @RequestParam(required = false) String description,
                                                    @RequestParam(required = false) MultipartFile[] files,
                                                    @RequestAttribute Session session) {
        int userId = session.getUserId();

        if (topic.isBlank()) {
            return new ResponseEntity<>(new ResponseError("Topic field can't be empty."), HttpStatus.BAD_REQUEST);
        } else if (topic.length() > 128) {
            return new ResponseEntity<>(new ResponseError("Max topic length = 128"), HttpStatus.BAD_REQUEST);
        } else if (!AdolfUtils.compareTimeDateFormat(taskTime)) {
            return new ResponseEntity<>(new ResponseError("Wrong date format."), HttpStatus.BAD_REQUEST);
        } else if (files != null) {
            if (files.length > 5) {
                return new ResponseEntity<>(new ResponseError("The max value of user files - 5"), HttpStatus.BAD_REQUEST);
            }
        }

        LocalDateTime created = LocalDateTime.now();
        LocalDateTime tasked = LocalDateTime.parse(taskTime, AdolfServerApplication.TIMEDATE_FORMAT);
        TodoElement element = new TodoElement(userId, topic, description, files, created, tasked);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/todo/" + element.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PostMapping(path = {"/todo/{tId}/files", "/todo/{tId}/files/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseError> createFile(@PathVariable String tId,
                                                    @RequestParam MultipartFile[] files,
                                                    @RequestAttribute Session session) {
        int userId = session.getUserId();
        int todoId = AdolfUtils.tryParseInteger(tId);
        TodoElement todoElement;

        if ((todoElement = TodoService.findByIdAndUserId(todoId, userId)) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (UserFilesService.findAllByTodoId(todoId).size() + files.length > 5) {
            return new ResponseEntity<>(new ResponseError("The max value of user files - 5"), HttpStatus.BAD_REQUEST);
        }

        HttpHeaders headers = new HttpHeaders();
        List<String> locations = new ArrayList<>();
        for (int id : todoElement.uploadFiles(files)) {
            locations.add("/todo/" + todoId + "/files/" + id);
        }
        headers.addAll("Location", locations);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(path = {"/todo", "/todo/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getTodo(@RequestAttribute Session session) {
        int userId = session.getUserId();
        return new ResponseEntity<>(TodoService.findAllJsonsByUserId(userId).toString(), HttpStatus.OK);
    }

    @GetMapping(path = {"/todo/{tId}/files", "/todo/{tId}/files/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getTodo(@PathVariable String tId,
                                          @RequestAttribute Session session) {
        int userId = session.getUserId();
        int todoId = AdolfUtils.tryParseInteger(tId);

        if (TodoService.findByIdAndUserId(todoId, userId) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(UserFilesService.findAllJsonByTodoId(todoId).toString(), HttpStatus.OK);
    }

    @GetMapping(path = {"/todo/{tId}/files/{fId}", "/todo/{tId}/files/{fId}/"})
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String tId,
                                                          @PathVariable String fId,
                                                          @RequestAttribute Session session) throws IOException {
        int userId = session.getUserId();
        int todoId = AdolfUtils.tryParseInteger(tId);
        int fileId = AdolfUtils.tryParseInteger(fId);
        UserFile file;

        if (TodoService.findByIdAndUserId(todoId, userId) == null
                || (file = UserFilesService.findByIdAndTodoId(fileId, todoId)) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Path path = Paths.get(String.format(AdolfServerApplication.FILE_PATH_TEMPLATE, todoId, file.getName()));
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @PatchMapping(path = {"/todo/{tId}", "/todo/{tId}/"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(path = {"/todo/{tId}", "/todo/{tId}/"},
            method = RequestMethod.POST,
            headers = {"X-HTTP-Method-Override=PATCH"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseError> updateTodo(@PathVariable String tId,
                                                    @RequestParam String topic,
                                                    @RequestParam(name = "task_time") String taskTime,
                                                    @RequestParam(required = false) String description,
                                                    @RequestAttribute Session session) {
        int userId = session.getUserId();
        int todoId = AdolfUtils.tryParseInteger(tId);
        TodoElement todoElement;

        if ((todoElement = TodoService.findByIdAndUserId(todoId, userId)) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (topic.isBlank()) {
            return new ResponseEntity<>(new ResponseError("Topic field can't be empty."), HttpStatus.BAD_REQUEST);
        } else if (topic.length() > 128) {
            return new ResponseEntity<>(new ResponseError("Max topic length = 128"), HttpStatus.BAD_REQUEST);
        } else if (!AdolfUtils.compareTimeDateFormat(taskTime)) {
            return new ResponseEntity<>(new ResponseError("Wrong date format."), HttpStatus.BAD_REQUEST);
        }

        LocalDateTime tasked = LocalDateTime.parse(taskTime, AdolfServerApplication.TIMEDATE_FORMAT);
        todoElement.setTopic(topic);
        todoElement.setTaskTime(tasked);
        todoElement.setDescription(description);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = {"/todo/{tId}/files/{fId}", "/todo/{tId}/files/{fId}"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseError> deleteFile(@PathVariable String tId,
                                                    @PathVariable String fId,
                                                    @RequestAttribute Session session) {
        int userId = session.getUserId();
        int todoId = AdolfUtils.tryParseInteger(tId);
        int fileId = AdolfUtils.tryParseInteger(fId);
        UserFile file;

        if (TodoService.findByIdAndUserId(todoId, userId) == null
                || (file = UserFilesService.findByIdAndTodoId(todoId, fileId)) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        file.deleteFile();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = {"/todo/{tId}", "/todo/{tId}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseError> deleteTodo(@PathVariable String tId,
                                                    @RequestAttribute Session session) {
        int userId = session.getUserId();
        int todoId = AdolfUtils.tryParseInteger(tId);
        TodoElement todoElement;

        if ((todoElement = TodoService.findByIdAndUserId(todoId, userId)) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        TodoService.delete(todoElement);
        todoElement.deleteLocalFiles();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ResponseError> multipartExceptionHandler(MultipartException ex) {
        return new ResponseEntity<>(new ResponseError("The summary file size can't be > "
                + AdolfServerApplication.MAX_FILE_SIZE + "MB"), HttpStatus.BAD_REQUEST);
    }

}
