package com.example.capstonecckma.controllers;

import com.example.capstonecckma.model.Doc;
import com.example.capstonecckma.model.Resource;
import com.example.capstonecckma.model.User;
import com.example.capstonecckma.repositories.ResourceRepository;
import com.example.capstonecckma.repositories.UserRepository;
import com.example.capstonecckma.services.EmailService;
import com.example.capstonecckma.services.DocStorageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ResourceController {
    private ResourceRepository resourceDao;
    private UserRepository userDao;

    private DocStorageService docStorageService;
    private final EmailService emailService;

    public ResourceController(ResourceRepository resourceDao, UserRepository userDao, DocStorageService docStorageService, EmailService emailService) {
        this.resourceDao = resourceDao;
        this.userDao = userDao;
        this.docStorageService = docStorageService;
        this.emailService = emailService;
    }

    // =================== Testing Page

    @GetMapping("/testing")
    public String testPage(Model vModel) {

        return "testpage";
    }

    // =================== Initial landing on index URL

    @GetMapping("/")
    public String getIndex(Model vModel) {

        return "index";
    }

    // =================== Landing page

    @GetMapping("/landing")
    public String getLanding(Model vModel) {

        return "landing-page-idea";
    }

    // =================== resources URL - view ALL resources

    @GetMapping("/resources")
    public String getResources(Model vModel) {
        List<Resource> resourceList = resourceDao.findAll();
        // pass posts to view
        vModel.addAttribute("resources", resourceList);
        return "resources/showall";
    }

    // =================== resources URL - view single resource

    @GetMapping("/resources/{id}")
    public String getResource(@PathVariable("id") long id, Model vModel) {
        Resource resource = resourceDao.findById(id).get();
        List<Doc> docs = docStorageService.getFiles();
        vModel.addAttribute("docs", docs);
        vModel.addAttribute("resource", resource);
        return "resources/showone";
    }

    // =================== resource CREATE

    @GetMapping("/create")
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
        return "multiupload";
    }

    // =================== resources EDIT/UPDATE

    @GetMapping("/resources/{id}/edit")
    public String getEditForm(@PathVariable("id") long id, Model model) {
        Resource resource = resourceDao.findById(id).get();
        model.addAttribute("resource", resource);
        return "resources/edit";
    }

    @PostMapping("/resources/{id}/edit")
    public String postEditForm(@ModelAttribute Resource resource) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        resource.setUser(principal);
        resourceDao.save(resource);
        return "redirect:/resources";
    }

    // =================== add single DOC to resource

    @GetMapping("/resources/individual-doc/{id}")
    public String resId(@PathVariable long id, Model model){
        Resource r = resourceDao.findById(id).get();

        model.addAttribute("r", r);
        return "singleupload";
    }

    @GetMapping("/hello")
    ResponseEntity<String> hello() {
        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }

    @GetMapping("/customHeader")
    ResponseEntity<String> customHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Custom-Header", "foo");

        return new ResponseEntity<>(
                "Custom header set", headers, HttpStatus.OK);
    }

    @PostMapping("/resources/{id}/like")
    @ResponseBody
    public String likeUnlikePost(@PathVariable long id) {
        Resource r = resourceDao.findById(id).get();
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userThatLiked = userDao.findById(principal.getId()).get();
        r.toggleUserLike(userThatLiked);
        resourceDao.save(r);
        return "Post liked!";
    }





}
