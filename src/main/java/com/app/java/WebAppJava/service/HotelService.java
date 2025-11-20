package com.app.java.WebAppJava.service;

import com.app.java.WebAppJava.model.Hotel;

import java.util.List;

public interface HotelService {

    List<Hotel> getAllHotels();

    List<Hotel> searchByCity(String city);

    Hotel getHotelById(Long id);

    void saveHotel(Hotel hotel);

    void deleteHotelById(Long id);
}
