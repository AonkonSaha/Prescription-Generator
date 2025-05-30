package com.example.prescription_generator.service;


import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

public interface UserValidationService {


    boolean isExitUserPassword( String contact, String password);
}
