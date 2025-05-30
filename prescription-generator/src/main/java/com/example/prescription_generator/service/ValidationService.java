package com.example.prescription_generator.service;

import com.example.prescription_generator.model.dto.DoctorDTO;
import com.example.prescription_generator.model.dto.UserDTO;
import org.springframework.stereotype.Service;

public interface ValidationService {

    String validateDoctorDetails(UserDTO userDTO);

}
