package net.mlk.adolfserver.security;

import jakarta.servlet.http.HttpServletRequest;
import net.mlk.adolfserver.data.session.Session;
import net.mlk.adolfserver.data.session.SessionRepository;
import net.mlk.adolfserver.data.session.SessionService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.regex.Pattern;

@Controller
public class SecurityController {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z0-9-_]+$");
    private static final long TOKEN_DURATION = 2592000;

    @PostMapping(path = {"/signup", "/signup/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signUp(@RequestParam("login") String name,
                                         @RequestParam("password") String password) {
        UserRepository repository = UserService.getUserRepository();
        if (!NAME_PATTERN.matcher(name).find() || name.length() < 4) {
            return new ResponseEntity<>(new ResponseError("Имя может содержать только A-Za-z0-9-_ и должно быть больше 4 символов.").toString(), HttpStatus.BAD_REQUEST);
        } else if (repository.existsByNameIgnoreCase(name)) {
            return new ResponseEntity<>(new ResponseError("Пользователь уже существует.").toString(), HttpStatus.CONFLICT);
        } else if (password.length() < 6) {
            return new ResponseEntity<>(new ResponseError("Минимальная длина пароля - 6 символов.").toString(), HttpStatus.BAD_REQUEST);
        }

        User user = new User(name, SecurityUtils.sha512Encrypt(password));
        Token token = new Token(name, true, TOKEN_DURATION);
        return new ResponseEntity<>(new UserEntity(name, token.getToken()).toString(), HttpStatus.OK);
    }

    @PostMapping(path = {"/signin", "/signin/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signIn(@RequestParam("login") String name,
                                         @RequestParam("password") String password) {
        UserRepository repository = UserService.getUserRepository();
        password = SecurityUtils.sha512Encrypt(password);

        User user = repository.findByNameIgnoreCase(name);

        if (user == null) {
            return new ResponseEntity<>(new ResponseError("Пользователь не найден.").toString(), HttpStatus.UNAUTHORIZED);
        } else if (!password.equals(user.getPassword())) {
            return new ResponseEntity<>(new ResponseError("Неверный пароль.").toString(), HttpStatus.UNAUTHORIZED);
        }

        Token token = new Token(name, true, TOKEN_DURATION);
        return new ResponseEntity<>(new UserEntity(name, token.getToken()).toString(), HttpStatus.OK);
    }

    @RequestMapping(path = {"/logout", "/logout/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> logout(HttpServletRequest request) {
        SessionRepository sessionRepository = SessionService.getSessionRepository();
        Session session = (Session) request.getAttribute("session");

        sessionRepository.delete(session);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private static class UserEntity {
        private String name;
        private String token;

        public UserEntity(String name, String token) {
            this.setName(name);
            this.setToken(token);
        }

        public Json getJson() {
            return new Json()
                    .append("name", this.name)
                    .append("token", this.token);
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getToken() {
            return this.token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        @Override
        public String toString() {
            return this.getJson().toString();
        }
    }

}
