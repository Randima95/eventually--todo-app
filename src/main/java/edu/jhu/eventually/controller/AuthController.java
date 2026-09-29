package edu.jhu.eventually.controller;

import edu.jhu.eventually.dto.RegistrationRequest;
import edu.jhu.eventually.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistration(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @Valid RegistrationRequest registrationRequest,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.register(registrationRequest);
            return "redirect:/login?registered";
        } catch (IllegalArgumentException exception) {
            model.addAttribute("registrationError", exception.getMessage());
            return "register";
        }
    }
}