package com.app.java.WebAppJava.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    @GetMapping("/login-user")
    public String loginUser() {
        return "login-user";
    }
}

