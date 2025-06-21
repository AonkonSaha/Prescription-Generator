package com.example.prescription_generator.exceptions;

public class InvalidPrescriptionArgumentException extends RuntimeException {
    public InvalidPrescriptionArgumentException(String message) {
        super(message);
    }
}
