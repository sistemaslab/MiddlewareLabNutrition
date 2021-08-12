package com.middleware.lab.controller;

import com.middleware.lab.model.db.User;
import com.middleware.provider.security.SecurityService;
import com.middleware.provider.security.UserService;
import com.middleware.provider.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @GetMapping("/api/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @PostMapping("/api/registration")
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(userForm);

        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());
        System.out.println("VA A REDIRIGIR");
        return "redirect:/api/welcome";
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("error", "Your username and password is invalid.");
        }

        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");

        }

        return "login";
    }

    @GetMapping({"/","/api/welcome"})
    public String welcome(Model model) {
        return "welcome";
    }

    @RequestMapping("/api/swagger")
    public String greeting() {
        return "redirect:/api/swagger-ui.html";
    }
}
