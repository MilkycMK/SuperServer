package net.mlk.adolfserver.api.todo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.mlk.adolfserver.AdolfServerApplication;
import net.mlk.adolfserver.data.session.Session;
import net.mlk.adolfserver.data.todo.TodoElement;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class TodoController {

    @PostMapping(path = {"/todo", "/todo/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> todoCreate(@RequestParam(value = "header") String header,
                                             @RequestParam(value = "task_time", required = false) String taskTime,
                                             @RequestParam(value = "description", required = false) String description,
                                             @RequestParam(value = "files", required = false) MultipartFile[] files,
                                             HttpServletRequest request,
                                             HttpServletResponse response) throws IOException {
        Session session = (Session) request.getAttribute("session");
        String name = session.getName();
        List<String> fileNames = new ArrayList<>();
        System.out.println(Arrays.toString(files));
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
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
