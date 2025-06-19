package com.example.prescription_generator.service;


import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

public interface UserValidationService {


    boolean isEmptyUserName(String name);
    boolean isExitUserByContact(String contact);
    boolean isEmptyUserContact(String contact);
    boolean isValidUserContactLength(String contact);
    boolean isValidUserPasswordLength(String password);
    boolean isValidEmailFormat(String email);
    boolean isValidGender(String gender);
    boolean isExitUserById(Long id);
    boolean isExitUserPassword(String contact,String password);
    boolean isEqualNewPassAndConfirmPass(String newPass, String confirmPass);
    boolean isEmptyDoctorDesignation(String designation);
    boolean isEmptyDoctorDegrees(Set<String> degrees);
    boolean isValidDoctorAges(LocalDate BirthDate);
    boolean isEmptyLincenseNumber(String licenseNumber);

    boolean isValidUserContactDigit(String mobileNumber);
}
