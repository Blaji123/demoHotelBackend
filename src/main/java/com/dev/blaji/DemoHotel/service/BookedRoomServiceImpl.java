package com.dev.blaji.DemoHotel.service;

import com.dev.blaji.DemoHotel.exception.InvalidBookingRequestException;
import com.dev.blaji.DemoHotel.exception.ResourceNotFoundException;
import com.dev.blaji.DemoHotel.model.BookedRoom;
import com.dev.blaji.DemoHotel.model.Room;
import com.dev.blaji.DemoHotel.repository.BookedRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookedRoomServiceImpl implements BookedRoomService {

    private final BookedRoomRepository bookingRepository;
    private final RoomService roomService;

    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    @Override
    public void cancelBooking(Long bookingId) {

        bookingRepository.deleteById(bookingId);
    }

    @Override
    public String saveBooking(Long roomId, BookedRoom bookingRequest) {
        if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
            throw new InvalidBookingRequestException("Check-in date must come before check-out date");
        }
        Room room = roomService.getRoomById(roomId).get();
        List<BookedRoom> existingBookings = room.getBookings();
        boolean roomIsAvailable = roomIsAvailable(bookingRequest, existingBookings);
        if(roomIsAvailable){
            room.addBookings(bookingRequest);
            bookingRepository.save(bookingRequest);
        }else{
            throw new InvalidBookingRequestException("This room has been booked for the selected date");
        }
        return bookingRequest.getBookingConfirmationCode();
    }

    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }

    @Override
    public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(()->new ResourceNotFoundException("No booking found with booking code :" + confirmationCode));
    }

    @Override
    public List<BookedRoom> getAllBookings() {
         return bookingRepository.findAll();
    }

    @Override
    public List<BookedRoom> getBookingByUserEmail(String email) {
        return bookingRepository.findByGuestEmail(email);
    }
}
