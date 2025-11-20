package com.app.java.WebAppJava.repository;

import com.app.java.WebAppJava.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByCityIgnoreCaseContaining(String city);
}
