package com.example.capstonecckma.controllers;

import com.example.capstonecckma.repositories.ResourceRepository;
import com.example.capstonecckma.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @Autowired
    private ResourceRepository resourceDao;
    @Autowired
    private UserRepository userDao;

    public ErrorController(ResourceRepository resourceDao, UserRepository userDao) {
        this.resourceDao = resourceDao;
        this.userDao = userDao;
    }

    @GetMapping("/error")
    public String getError() {
        return "redirect:error/4xx";
    }
}
