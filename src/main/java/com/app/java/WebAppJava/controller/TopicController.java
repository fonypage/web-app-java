package com.app.java.WebAppJava.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TopicController {

    @GetMapping("/theory")
    public String theory(){
        return "theory";
    }

    @GetMapping("/practice")
    public String practice(){
        return "practice";
    }
}
