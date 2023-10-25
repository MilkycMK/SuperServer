package net.mlk.adolfserver.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.mlk.adolfserver.data.user.User;
import net.mlk.adolfserver.data.user.UserRepository;
import net.mlk.adolfserver.data.user.UserService;
import net.mlk.adolfserver.errors.ResponseError;
import net.mlk.adolfserver.security.utils.SecurityUtils;
import net.mlk.jmson.Json;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.regex.Pattern;

@Controller
public class SecurityController {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z0-9-_]+$");

    @PostMapping(path = {"/signup", "/signup/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signUp(@RequestParam("login") String name,
                                 @RequestParam("password") String password,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        UserRepository repository = UserService.getUserRepository();
        if (!NAME_PATTERN.matcher(name).find() || name.length() < 4) {
            return new ResponseEntity<>(new ResponseError("Имя может содержать только A-Za-z0-9-_ и должно быть больше 4 символов.").toString(), HttpStatus.BAD_REQUEST);
        } else if (repository.existsUserByNameIgnoreCase(name)) {
            return new  ResponseEntity<>(new ResponseError("Пользователь уже существует.").toString(), HttpStatus.CONFLICT);
        } else if (password.length() < 6) {
            return new ResponseEntity<>(new ResponseError("Минимальная длина пароля - 6 символов.").toString(), HttpStatus.BAD_REQUEST);
        }

        User user = new User(name, SecurityUtils.sha512Encrypt(password));
        Token token = new Token(name, true, 2592000);
        Json json = new Json()
                .append("name", name)
                .append("token", token.getToken());
        return new ResponseEntity<>(json.toString(), HttpStatus.OK);
    }

    @PostMapping(path = {"/signin", "/signin/"})
    public void signIn(@RequestParam("login") String login,
                       @RequestParam("password") String password) {
        System.out.println(true);
    }

}
