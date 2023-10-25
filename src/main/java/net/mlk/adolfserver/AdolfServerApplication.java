package net.mlk.adolfserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.time.format.DateTimeFormatter;

@Controller
@SpringBootApplication
public class AdolfServerApplication {
    public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        File file = new File("userFiles");
        if (!file.exists()) {
            file.mkdir();
        }
        SpringApplication.run(AdolfServerApplication.class, args);
    }

    @RequestMapping(path = {"", "/"})
    public String index() {
        return "test";
    }

}
