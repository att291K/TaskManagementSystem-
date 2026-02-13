package authservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/custom-login")
    public String customLogin() {
        return "custom-login";  // имя HTML файла без расширения
    }
}
