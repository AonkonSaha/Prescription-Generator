package com.example.prescription_generator.exceptions;

public class InvalidPrescriptionException extends RuntimeException {
    public InvalidPrescriptionException(String message) {
        super(message);
    }
}
