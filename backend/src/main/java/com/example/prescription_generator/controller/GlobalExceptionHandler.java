package com.example.prescription_generator.controller;

import com.example.prescription_generator.exceptions.*;
import com.example.prescription_generator.model.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidUserArgumentException.class)
    public ResponseEntity<ErrorResponse> handleUserValidationException(InvalidUserArgumentException exception, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_ACCEPTABLE, exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserExitException(UserNotFoundException exception, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(InvalidLoginArgumentException.class)
    public ResponseEntity<ErrorResponse> handleUserCredentialException(InvalidLoginArgumentException exception, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(InvalidPrescriptionArgumentException.class)
    public ResponseEntity<ErrorResponse> handlePrescriptionValidationException(InvalidPrescriptionArgumentException exception, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_ACCEPTABLE, exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(PrescriptionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePrescriptionExitException(PrescriptionNotFoundException exception, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, exception.getMessage(), request.getRequestURI());
    }

    private ResponseEntity<ErrorResponse> buildError(HttpStatus status, String message, String path) {
        ErrorResponse response = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, status);
    }
}


