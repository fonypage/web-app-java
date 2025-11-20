package com.app.java.WebAppJava.controller;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.UUID;

@Component
public class CookieUtil {
    public static final String COOKIE_NAME = "USER_SESSION";

    public String getSessionId(HttpServletRequest req) {
        if (req.getCookies() == null) return null;
        return Arrays.stream(req.getCookies())
                .filter(c -> COOKIE_NAME.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    public void ensureSessionCookie(HttpServletRequest req, HttpServletResponse resp) {
        String sid = getSessionId(req);
        if (sid == null) {
            sid = UUID.randomUUID().toString();
            Cookie c = new Cookie(COOKIE_NAME, sid);
            c.setPath("/");
            c.setMaxAge(60*60*24*365); // год
            resp.addCookie(c);
        }
    }
}
