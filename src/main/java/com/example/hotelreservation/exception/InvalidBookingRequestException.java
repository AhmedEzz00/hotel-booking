package com.example.hotelreservation.exception;

public class InvalidBookingRequestException extends  RuntimeException {
    public InvalidBookingRequestException(String message){
        super(message);
    }
}
