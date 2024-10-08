package net.mlk.adolfserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class AdolfServerApplication {

    public static final int MAX_FILE_SIZE = 10;
    public static final String FILE_PATH_TEMPLATE = "userFiles/%s/%s";
    public static final String FILES_PATH_TEMPLATE = "userFiles/%s";
    public static final DateTimeFormatter TIMEDATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        SpringApplication.run(AdolfServerApplication.class, args);
    }

}
