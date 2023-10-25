package net.mlk.adolfserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@SpringBootApplication
public class AdolfServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdolfServerApplication.class, args);
    }

    @RequestMapping(path = {"", "/"})
    public String index() {
        return "test";
    }

}
