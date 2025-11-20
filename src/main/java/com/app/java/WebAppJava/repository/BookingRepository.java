package com.app.java.WebAppJava.repository;

import com.app.java.WebAppJava.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBySessionIdOrderByCreatedAtDesc(String sessionId);
}
