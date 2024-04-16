package com.dev.blaji.DemoHotel.service;

import com.dev.blaji.DemoHotel.model.BookedRoom;

import java.util.List;

public interface BookedRoomService {
    void cancelBooking(Long bookingId);

    String saveBooking(Long roomId, BookedRoom bookingRequest);

    BookedRoom findByBookingConfirmationCode(String confirmationCode);

    List<BookedRoom> getAllBookings();

    List<BookedRoom> getBookingByUserEmail(String email);
}
