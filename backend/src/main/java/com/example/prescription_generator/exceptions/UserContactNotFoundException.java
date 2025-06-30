package com.example.prescription_generator.exceptions;

public class UserContactNotFoundException extends RuntimeException {
    public UserContactNotFoundException(String message) {
        super(message);
    }
}
