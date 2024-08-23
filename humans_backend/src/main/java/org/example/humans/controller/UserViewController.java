package org.example.humans.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserViewController {
    @GetMapping("/account/login")
    public String login(){
        return "login";
    }

    @GetMapping("/account/signup")
    public String signup(){
        return "sign-up";
    }
}
