package com.app.java.WebAppJava.controller;

import com.app.java.WebAppJava.dto.ProfileChartsDto;
import com.app.java.WebAppJava.service.ProfileChartsService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileChartsController {

    private final ProfileChartsService chartsService;

    public ProfileChartsController(ProfileChartsService chartsService) {
        this.chartsService = chartsService;
    }

    @GetMapping("/charts")
    public ProfileChartsDto charts(@RequestParam(defaultValue = "30") int days) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return chartsService.build(username, days);
    }
}