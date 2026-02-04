package ru.edu.taskmanagementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/")
public class DefaultRestController {

    @GetMapping
    public String redirectTasks() {
        return "redirect:/tasks/getAllTasks";
    }
}
