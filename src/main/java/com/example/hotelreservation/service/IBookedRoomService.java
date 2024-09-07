package com.example.hotelreservation.service;

import com.example.hotelreservation.model.BookedRoom;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IBookedRoomService {
    List<BookedRoom> getAllBookingsByRoomId(Long id);

    BookedRoom findByBookingConfirmationcode(String conirmationCode);

    String saveBooking(Long roomId, BookedRoom bookingRequest);

    void cancleBooking(Long bookingId);

    List<BookedRoom> getAllBookings();
}
