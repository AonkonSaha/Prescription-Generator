package com.example.prescription_generator.service;

import com.example.prescription_generator.model.dto.PrescriptionDTO;
import com.example.prescription_generator.model.dto.UserDTO;

public interface ValidationService {

    String validateDoctorDetails(UserDTO userDTO);
    String validatePrescriptionDetails(PrescriptionDTO prescriptionDTO);
}
