package com.app.java.WebAppJava.controller;

import com.app.java.WebAppJava.service.BookingService;
import com.app.java.WebAppJava.model.Booking;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.*;
import java.util.List;

@Controller
public class MyBookingsController {

    private final BookingService bookingService;
    private final CookieUtil cookieUtil;

    public MyBookingsController(BookingService bookingService, CookieUtil cookieUtil) {
        this.bookingService = bookingService;
        this.cookieUtil = cookieUtil;
    }

    @GetMapping("/my-bookings")
    public String myBookings(HttpServletRequest req, Model model) {

        String sessionId = cookieUtil.getSessionId(req);

        List<Booking> list = bookingService.getBookingsForSession(sessionId);

        model.addAttribute("bookings", list);

        return "my-bookings"; // позже создадим шаблон
    }

    @PostMapping("/my-bookings/delete/{id}")
    public String deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return "redirect:/my-bookings";
    }
}
