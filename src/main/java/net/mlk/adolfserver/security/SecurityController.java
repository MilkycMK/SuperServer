package net.mlk.adolfserver.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SecurityController {

    @PostMapping(path = {"/signup", "/signup/"})
    public void signUp(@RequestParam("login") String login,
                       @RequestParam("password") String password) {

    }

    @PostMapping(path = {"/signin", "/signin/"})
    public void signIn(@RequestParam("login") String login,
                       @RequestParam("password") String password) {

    }


}
