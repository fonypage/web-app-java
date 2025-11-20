package com.app.java.WebAppJava.service;

import com.app.java.WebAppJava.model.Room;

import java.util.List;

public interface RoomService {

    List<Room> getRoomsByHotel(Long hotelId);

    Room getRoomById(Long id);

    Room saveRoom(Room room);

    void deleteRoomById(Long id);
}
