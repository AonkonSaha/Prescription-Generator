package com.example.prescription_generator.exceptions;

public class InvalidUserArgumentException extends RuntimeException {
  public InvalidUserArgumentException(String message) {
    super(message);
  }
}
