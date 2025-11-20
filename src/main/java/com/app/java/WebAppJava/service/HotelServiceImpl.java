package com.app.java.WebAppJava.service;

import com.app.java.WebAppJava.model.Hotel;
import com.app.java.WebAppJava.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    public HotelServiceImpl(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @Override
    public List<Hotel> searchByCity(String city) {
        if (city == null || city.isBlank()) {
            return hotelRepository.findAll();
        }
        return hotelRepository.findByCityIgnoreCaseContaining(city);
    }

    @Override
    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found: " + id));
    }

    @Override
    public void saveHotel(Hotel hotel) {
        if (hotel.getId() == null) {
            // создаём новый отель (rooms пустой — это ок, у нового их ещё нет)
            hotelRepository.save(hotel);
        } else {
            // редактируем существующий
            Hotel existing = hotelRepository.findById(hotel.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Отель не найден: " + hotel.getId()));

            existing.setName(hotel.getName());
            existing.setCity(hotel.getCity());
            existing.setDescription(hotel.getDescription());
            existing.setRating(hotel.getRating());
            existing.setImageUrl(hotel.getImageUrl());

            // ВАЖНО: rooms НЕ трогаем
            hotelRepository.save(existing);
        }
    }

    @Override
    public void deleteHotelById(Long id) {
        hotelRepository.deleteById(id);
    }
}
