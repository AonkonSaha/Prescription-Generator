package com.example.prescription_generator.controller;

import com.example.prescription_generator.exceptions.*;
import com.example.prescription_generator.model.dto.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InvalidUserArgumentException.class)
    public ResponseEntity<ErrorResponse> handleUserValidationException(InvalidUserArgumentException exception,
                                                                       HttpServletRequest request) {
        log.error("Invalid user argument exception: {}", exception.getMessage());
        return buildError(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserExitException(UserNotFoundException exception,
                                                                 HttpServletRequest request) {
        log.error("User not found exception: {}", exception.getMessage());
        return buildError(HttpStatus.NOT_FOUND, exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler({InvalidLoginArgumentException.class , BadCredentialsException.class})
    public ResponseEntity<ErrorResponse> handleUserCredentialException(Exception exception,
                                                                       HttpServletRequest request) {
        log.error("Invalid login argument exception: {}", exception.getMessage());
        return buildError(HttpStatus.UNAUTHORIZED, exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorResponse> handleJWTFilterException(SignatureException exception,
                                                                  HttpServletRequest request) {
        log.error("JWT signature exception: {}", exception.getMessage());
        return buildError(HttpStatus.UNAUTHORIZED,
                "JWT signature not valid!",
                request.getRequestURI());
    }
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleJWTFilterException(ExpiredJwtException exception,
                                                                  HttpServletRequest request) {
        log.error("JWT token expired exception: {}", exception.getMessage());
        return buildError(HttpStatus.UNAUTHORIZED,
                "JWT token already expired!",
                request.getRequestURI());
    }
    @ExceptionHandler(UserContactNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleJWTFilterException(UserContactNotFoundException exception,
                                                                  HttpServletRequest request) {
        log.error("User contact not found exception: {}", exception.getMessage());
        return buildError(HttpStatus.UNAUTHORIZED,
                exception.getMessage(),
                request.getRequestURI());
    }
    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ErrorResponse> handleJWTFilterException(MalformedJwtException  exception,
                                                                  HttpServletRequest request) {
        log.error("JWT malformed exception: {}", exception.getMessage());
        return buildError(HttpStatus.UNAUTHORIZED,
                "JWT is not correctly constructed",
                request.getRequestURI());
    }
    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<ErrorResponse> handleJWTFilterException(UnsupportedJwtException exception,
                                                                  HttpServletRequest request) {
        log.error("JWT unsupported exception: {}", exception.getMessage());
        return buildError(HttpStatus.UNAUTHORIZED,
                "JWT uses an unsupported format or algorithm",
                request.getRequestURI());
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleJWTFilterException(AccessDeniedException exception,
                                                                  HttpServletRequest request) {
        log.error("Access denied exception: {}", exception.getMessage());
        return buildError(HttpStatus.FORBIDDEN,
                "Not Authorized to access this resource!",
                request.getRequestURI());
    }

    @ExceptionHandler(InvalidPrescriptionArgumentException.class)
    public ResponseEntity<ErrorResponse> handlePrescriptionValidationException(InvalidPrescriptionArgumentException exception,
                                                                               HttpServletRequest request) {
        log.error("Invalid prescription argument exception: {}", exception.getMessage());
        return buildError(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(PrescriptionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePrescriptionExitException(PrescriptionNotFoundException exception,
                                                                         HttpServletRequest request) {
        log.error("Prescription not found exception: {}", exception.getMessage());
        return buildError(HttpStatus.NOT_FOUND, exception.getMessage(), request.getRequestURI());
    }
    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoleException(RoleNotFoundException exception,
                                                             HttpServletRequest request) {
        log.error("Role not found exception: {}", exception.getMessage());
        return buildError(HttpStatus.NOT_FOUND, exception.getMessage(), request.getRequestURI());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleRoleException(Exception exception, HttpServletRequest request) {
        log.error("Unexpected exception: {}", exception.getMessage());
        String message = exception.getMessage() != null ? exception.getMessage() : "An unexpected error occurred";
        return buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                message,
                request.getRequestURI()
        );
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


