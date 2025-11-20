package com.app.java.WebAppJava.controller;

import com.app.java.WebAppJava.model.Booking;
import com.app.java.WebAppJava.model.Room;
import com.app.java.WebAppJava.service.BookingService;
import com.app.java.WebAppJava.service.RoomService;
import com.app.java.WebAppJava.controller.CookieUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;

@Controller
public class BookingController {

    private final BookingService bookingService;
    private final RoomService roomService;
    private final CookieUtil cookieUtil;

    public BookingController(BookingService bookingService, RoomService roomService, CookieUtil cookieUtil) {
        this.bookingService = bookingService;
        this.roomService = roomService;
        this.cookieUtil = cookieUtil;
    }

    // страница оформления брони
    @GetMapping("/booking/{roomId}")
    public String bookingPage(@PathVariable Long roomId, Model model) {

        Room room = roomService.getRoomById(roomId);

        model.addAttribute("room", room);

        return "booking"; // шаблон booking.html создадим позже
    }

    // обработка брони
    @PostMapping("/booking")
    public String createBooking(
            @RequestParam Long roomId,
            @RequestParam String checkIn,
            @RequestParam String checkOut,
            @RequestParam Integer guests,
            HttpServletRequest req,
            HttpServletResponse resp,
            Model model
    ) {

        // создать куку с sessionId, если нет
        cookieUtil.ensureSessionCookie(req, resp);
        String sessionId = cookieUtil.getSessionId(req);

        Room room = roomService.getRoomById(roomId);

        // Базовые вещи всегда кладём в модель, чтобы при ошибке вернуть форму
        model.addAttribute("room", room);

        // ---- ВАЛИДАЦИЯ ----
        LocalDate in;
        LocalDate out;

        try {
            in = LocalDate.parse(checkIn);
            out = LocalDate.parse(checkOut);
        } catch (Exception e) {
            model.addAttribute("error", "Некорректный формат дат. Пожалуйста, выберите даты заезда и выезда.");
            return "booking";
        }

        if (!out.isAfter(in)) {
            model.addAttribute("error", "Дата выезда должна быть позже даты заезда.");
            return "booking";
        }

        if (guests == null || guests < 1) {
            model.addAttribute("error", "Количество гостей должно быть не меньше 1.");
            return "booking";
        }

        if (room.getCapacity() != null && guests > room.getCapacity()) {
            model.addAttribute("error", "Количество гостей не может превышать вместимость номера.");
            return "booking";
        }

        long days = ChronoUnit.DAYS.between(in, out);
        if (days <= 0) {
            model.addAttribute("error", "Неверный диапазон дат.");
            return "booking";
        }

        BigDecimal total = room.getPricePerNight().multiply(BigDecimal.valueOf(days));

        // ---- СОЗДАНИЕ БРОНИ ----
        Booking booking = new Booking();
        booking.setSessionId(sessionId);
        booking.setRoom(room);
        booking.setCheckIn(in);
        booking.setCheckOut(out);
        booking.setGuests(guests);
        booking.setStatus("NEW");
        booking.setPaymentStatus("UNPAID");
        booking.setTotalPrice(total);

        bookingService.createBooking(booking);

        model.addAttribute("booking", booking);
        model.addAttribute("room", room);

        return "booking-result";
    }
}
