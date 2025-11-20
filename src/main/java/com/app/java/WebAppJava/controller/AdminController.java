package com.app.java.WebAppJava.controller;

import com.app.java.WebAppJava.model.Hotel;
import com.app.java.WebAppJava.model.Room;
import com.app.java.WebAppJava.service.HotelService;
import com.app.java.WebAppJava.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final HotelService hotelService;
    private final RoomService roomService;

    public AdminController(HotelService hotelService, RoomService roomService) {
        this.hotelService = hotelService;
        this.roomService = roomService;
    }

    // ====== ОТЕЛИ ======

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("hotels", hotelService.getAllHotels());
        return "admin/dashboard"; // шаблон dashboard.html
    }

    @GetMapping("/hotels/new")
    public String newHotel(Model model) {
        model.addAttribute("hotel", new Hotel());
        return "admin/hotel-form";
    }

    @GetMapping("/hotels/edit/{id}")
    public String editHotel(@PathVariable Long id, Model model) {
        Hotel hotel = hotelService.getHotelById(id);
        model.addAttribute("hotel", hotel);
        model.addAttribute("rooms", roomService.getRoomsByHotel(id));
        return "admin/hotel-form";
    }

    @PostMapping("/hotels/save")
    public String saveHotel(@ModelAttribute("hotel") Hotel hotel) {
        hotelService.saveHotel(hotel);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/hotels/delete/{id}")
    public String deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotelById(id);
        return "redirect:/admin/dashboard";
    }

    // ====== НОМЕРА ======

    @GetMapping("/hotels/{hotelId}/rooms/new")
    public String newRoom(@PathVariable Long hotelId, Model model) {
        Hotel hotel = hotelService.getHotelById(hotelId);
        Room room = new Room();
        room.setHotel(hotel);

        model.addAttribute("hotel", hotel);
        model.addAttribute("room", room);

        return "admin/room-form";
    }

    @GetMapping("/rooms/edit/{id}")
    public String editRoom(@PathVariable Long id, Model model) {
        Room room = roomService.getRoomById(id);
        Hotel hotel = room.getHotel();

        model.addAttribute("hotel", hotel);
        model.addAttribute("room", room);

        return "admin/room-form";
    }

    @PostMapping("/rooms/save")
    public String saveRoom(
            @RequestParam("hotelId") Long hotelId,
            @ModelAttribute("room") Room room
    ) {
        Hotel hotel = hotelService.getHotelById(hotelId);
        room.setHotel(hotel);
        roomService.saveRoom(room);
        return "redirect:/admin/hotels/edit/" + hotelId;
    }

    @GetMapping("/rooms/delete/{id}")
    public String deleteRoom(@PathVariable Long id) {
        Room room = roomService.getRoomById(id);
        Long hotelId = room.getHotel().getId();
        roomService.deleteRoomById(id);
        return "redirect:/admin/hotels/edit/" + hotelId;
    }
}
