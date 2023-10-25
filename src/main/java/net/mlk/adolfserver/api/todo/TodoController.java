package net.mlk.adolfserver.api.todo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.mlk.adolfserver.data.session.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
//        AdolfServerApplication.FORMAT

@Controller
public class TodoController {

    /*
        files - для отправки необходим тип multipart потом напишу пример для фронта
     */
    @PostMapping(path = {"/todo", "/todo/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> todoCreate(@RequestParam(value = "header", required = false) String header,
                                             @RequestParam(value = "creation_time", required = false) String creationTime,
                                             @RequestParam(value = "task_time", required = false) String taskTime,
                                             @RequestParam(value = "description", required = false) String description,
                                             @RequestParam(value = "files", required = false) MultipartFile[] files,
                                             HttpServletRequest request,
                                             HttpServletResponse response) {
        Session session = (Session) request.getAttribute("session");
//        System.out.println(header);
//        System.out.println(description);
//        System.out.println(Arrays.toString(files));
//        for (MultipartFile file : files) {
//            FileOutputStream stream = new FileOutputStream(file.getOriginalFilename());
//            stream.write(file.getBytes());
//        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
