package com.app.java.WebAppJava.controller;

import com.app.java.WebAppJava.model.Room;
import com.app.java.WebAppJava.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // Страница номера
    @GetMapping("/room/{id}")
    public String roomDetail(@PathVariable Long id, Model model) {

        Room room = roomService.getRoomById(id);

        model.addAttribute("room", room);

        return "room-detail"; // шаблон сделаем позже
    }
}
