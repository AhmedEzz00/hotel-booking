package com.example.hotelreservation.exception;

public class RoleAlreadyExistException extends RuntimeException{
    public RoleAlreadyExistException(String s) {
        super(s);
    }
}
