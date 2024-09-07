package com.example.hotelreservation.service;

import com.example.hotelreservation.exception.InvalidBookingRequestException;
import com.example.hotelreservation.exception.ResourceNotFoundException;
import com.example.hotelreservation.model.BookedRoom;
import com.example.hotelreservation.model.Room;
import com.example.hotelreservation.repository.IBookedRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookedRoomServiceImpl implements IBookedRoomService{

    private final IBookedRoomRepository bookedRoomRepository;
    private final IRoomService roomService;

    @Override
    public List<BookedRoom> getAllBookings() {
        return bookedRoomRepository.findAll();
    }

    @Override
    public List<BookedRoom> getAllBookingsByRoomId(Long id) {
        return bookedRoomRepository.findByRoomId(id);
    }

    @Override
    public BookedRoom findByBookingConfirmationcode(String confirmationCode) {
        return bookedRoomRepository.findByBookingConfirmationCode(confirmationCode)
                .orElseThrow(()-> new ResourceNotFoundException("no booking found with booking code: "+confirmationCode));
    }

    @Override
    public String saveBooking(Long roomId, BookedRoom bookingRequest) {
        if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
            throw  new InvalidBookingRequestException("Check-in date must be before check-out date");
        }
        Room room= roomService.getRoomById(roomId).get();
        List<BookedRoom> existingBookings= room.getBookings();
        boolean roomIsAvailale= roomIsAvailable(bookingRequest, existingBookings);
        if (roomIsAvailale){
            room.addBooking(bookingRequest);
            bookedRoomRepository.save(bookingRequest);
        }else {
            throw new InvalidBookingRequestException("Sorry, this room is not available for the selected dates" );
        }
        return bookingRequest.getBookingConfirmationCode();
    }

    @Override
    public void cancleBooking(Long bookingId) {
        bookedRoomRepository.deleteById(bookingId);
    }

    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                                   bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                || bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate())

                                || (bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                        && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate())
                                        && bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate()))
                );
    }


}
