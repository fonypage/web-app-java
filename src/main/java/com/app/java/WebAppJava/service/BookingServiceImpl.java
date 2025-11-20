package com.app.java.WebAppJava.service;

import com.app.java.WebAppJava.model.Booking;
import com.app.java.WebAppJava.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Booking createBooking(Booking booking) {
        // здесь позже будем считать цену, статус, дергать Cloud Function и т.д.
        if (booking.getStatus() == null) {
            booking.setStatus("NEW");
        }
        if (booking.getPaymentStatus() == null) {
            booking.setPaymentStatus("UNPAID");
        }
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getBookingsForSession(String sessionId) {
        if (sessionId == null) {
            return List.of();
        }
        return bookingRepository.findBySessionIdOrderByCreatedAtDesc(sessionId);
    }

    @Override
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}
