package com.app.java.WebAppJava.controller;

import com.app.java.WebAppJava.model.UserProfile;
import com.app.java.WebAppJava.repository.UserProfileRepository;
import com.app.java.WebAppJava.service.EmailOtpService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class EmailAuthController {

    private final EmailOtpService otpService;
    private final UserProfileRepository userProfileRepository;

    public EmailAuthController(EmailOtpService otpService, UserProfileRepository userProfileRepository) {
        this.otpService = otpService;
        this.userProfileRepository = userProfileRepository;
    }

    @PostMapping("/auth/email/request")
    public String request(@RequestParam String email, RedirectAttributes ra) {
        otpService.requestCode(email);
        ra.addAttribute("email", email);
        ra.addFlashAttribute("info", "Код отправлен на почту ✅");
        return "redirect:/login-user-code";
    }

    @GetMapping("/login-user-code")
    public String codePage(@RequestParam String email, Model model,
                           @ModelAttribute("error") String error) {
        model.addAttribute("email", email);
        if (error != null && !error.isBlank()) model.addAttribute("error", error);
        return "login-user-code";
    }

    @PostMapping("/auth/email/verify")
    public String verify(@RequestParam String email,
                         @RequestParam String code,
                         HttpServletRequest request,
                         RedirectAttributes ra) {

        boolean ok = otpService.verifyCode(email, code);
        if (!ok) {
            ra.addAttribute("email", email);
            ra.addFlashAttribute("error", "Неверный код или он истёк");
            return "redirect:/login-user-code";
        }

        String username = email.trim().toLowerCase();

        // если профиля нет — создаём (username = email)
        userProfileRepository.findByUsername(username).orElseGet(() -> {
            UserProfile p = new UserProfile();
            p.setUsername(username);
            return userProfileRepository.save(p);
        });

        // логин в сессию как ROLE_USER
        List<GrantedAuthority> auths = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        Authentication auth = new UsernamePasswordAuthenticationToken(username, null, auths);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        request.getSession(true).setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                context
        );

        return "redirect:/my-tests";
    }
}