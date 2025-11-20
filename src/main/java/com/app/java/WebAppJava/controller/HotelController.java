package com.app.java.WebAppJava.controller;

import com.app.java.WebAppJava.service.HotelService;
import com.app.java.WebAppJava.service.RoomService;
import com.app.java.WebAppJava.model.Hotel;
import com.app.java.WebAppJava.model.Room;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HotelController {

    private final HotelService hotelService;
    private final RoomService roomService;

    public HotelController(HotelService hotelService, RoomService roomService) {
        this.hotelService = hotelService;
        this.roomService = roomService;
    }

    // список отелей (главная логика поиска)
    @GetMapping("/hotels")
    public String hotels(
            @RequestParam(required = false) String city,
            Model model
    ) {
        List<Hotel> hotels = hotelService.searchByCity(city);

        model.addAttribute("hotels", hotels);
        model.addAttribute("city", city);

        return "hotels"; // шаблон hotels.html создадим позже
    }

    // конкретный отель со списком номеров
    @GetMapping("/hotel/{id}")
    public String hotelDetail(@PathVariable Long id, Model model) {

        Hotel hotel = hotelService.getHotelById(id);
        List<Room> rooms = roomService.getRoomsByHotel(id);

        model.addAttribute("hotel", hotel);
        model.addAttribute("rooms", rooms);

        return "hotel-detail"; // шаблон позже создадим
    }
}
