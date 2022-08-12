package com.example.capstonecckma.controllers;

import com.example.capstonecckma.model.Resource;
import com.example.capstonecckma.model.User;
import com.example.capstonecckma.repositories.ResourceRepository;
import com.example.capstonecckma.repositories.UserRepository;
import com.example.capstonecckma.services.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ResourceController {
    private ResourceRepository resourceDao;
    private UserRepository userDao;
    private final EmailService emailService;

    public ResourceController(ResourceRepository resourceDao, UserRepository userDao, EmailService emailService) {
        this.resourceDao = resourceDao;
        this.userDao = userDao;
        this.emailService = emailService;
    }

//    index page mapping
    @GetMapping("/")
    public String getIndex() {
        return "index";
    }

//    view of all resources
    @GetMapping("/resources")
    public String getResources(Model model) {

        model.addAttribute("resources", resourceDao.findAll());

        return "resources/index";
    }

//    view a single resource
    @GetMapping("/resources/{id}")
    public String getResource(@PathVariable("id") long id, Model model) {
        Resource resource = resourceDao.findById(id).get();
        model.addAttribute("resource", resource);
        return "resources/show";
    }

//    create resource
    @GetMapping("/resources/create")
    public String getCreateForm(Model model) {
        model.addAttribute("resource", new Resource());
        return "resources/create";
    }

    @PostMapping("/resources/create")
    public String postCreateForm(@ModelAttribute Resource resource) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        resource.setUser(principal);
        String emailSubject = "New Resource Added";
        String emailBlurb = "Thank you for uploading a new resource. The resource is titled \r\n["
                + resource.getTitle() + "].\r\nIf this was not expected, please contact customer support.";
        String emailTo = principal.getEmail();
        resourceDao.save(resource);
        emailService.prepareAndSend(emailSubject, emailBlurb, emailTo);
        return "redirect:/resources/index";
    }

//    edit resource
    @GetMapping("/resources/{id}/edit")
    public String getEditForm(@PathVariable("id") long id, Model model) {
        Resource resource = resourceDao.findById(id).get();
        model.addAttribute("resource", resource);
        return "resources/edit";
    }

    @PostMapping("/resources/{id}/edit")
    public String postEditForm(@ModelAttribute Resource resource) {
        resourceDao.save(resource);
        return "redirect:/resources/index";
    }





}