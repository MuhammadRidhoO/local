package task.company.local.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TodoOtherController {
    @GetMapping("/")
    public String todo() {
        return "login";
    }
}
