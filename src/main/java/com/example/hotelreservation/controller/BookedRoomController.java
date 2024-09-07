package com.example.hotelreservation.controller;

import com.example.hotelreservation.exception.InvalidBookingRequestException;
import com.example.hotelreservation.exception.ResourceNotFoundException;
import com.example.hotelreservation.model.BookedRoom;
import com.example.hotelreservation.model.Room;
import com.example.hotelreservation.responsne.BookingResponse;
import com.example.hotelreservation.responsne.RoomResponse;
import com.example.hotelreservation.service.IBookedRoomService;
import com.example.hotelreservation.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookedRoomController {
    private final IBookedRoomService bookingService;
    private final IRoomService roomService;

    @GetMapping("/all-bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings(){
        List<BookedRoom> bookings = bookingService.getAllBookings();
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for(BookedRoom booking: bookings){
            BookingResponse bookingResponse= getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }

    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode){
        try {
            BookedRoom booking= bookingService.findByBookingConfirmationcode(confirmationCode);
            BookingResponse bookingResponse= getBookingResponse(booking);
            return ResponseEntity.ok(bookingResponse);
        }catch(ResourceNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping("/room/{roomId}/booking")
    public  ResponseEntity<?> saveBooking(@PathVariable Long roomId,
                                          @RequestBody BookedRoom bookingRequest){
        try {
            String confirmationCode = bookingService.saveBooking(roomId, bookingRequest);
            return ResponseEntity.ok("Room booked successfully ! your booking confirmation code is: "+ confirmationCode);
        }catch(InvalidBookingRequestException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/booking/{bookingId}/delete")
    public void cancleBooking(@PathVariable Long bookingId){
        bookingService.cancleBooking(bookingId);
    }

    private BookingResponse getBookingResponse(BookedRoom booking) {
        Room bookedRoom= roomService.getRoomById(booking.getRoom().getId()).get();
        RoomResponse roomResponse= new RoomResponse(bookedRoom.getId(),
                bookedRoom.getRoomType(),
                bookedRoom.getRoomPrice());
        return new BookingResponse(booking.getId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getGuestFullName(),
                booking.getGuestEmail(),
                booking.getNumOfAdults(),
                booking.getNumOfChildren(),
                booking.getTotalNumOfGuests(),
                booking.getBookingConfirmationCode(),
                roomResponse);
    }

}
