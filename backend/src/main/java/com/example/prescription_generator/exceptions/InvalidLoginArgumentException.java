package com.example.prescription_generator.exceptions;

public class InvalidLoginArgumentException extends RuntimeException {
    public InvalidLoginArgumentException(String message) {
        super(message);
    }
}
