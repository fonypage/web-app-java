package com.app.java.WebAppJava.service;

import com.app.java.WebAppJava.model.Booking;

import java.util.List;

public interface BookingService {

    Booking createBooking(Booking booking);

    List<Booking> getBookingsForSession(String sessionId);

    void deleteBooking(Long id);
}
