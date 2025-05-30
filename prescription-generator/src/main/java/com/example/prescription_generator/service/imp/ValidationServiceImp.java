package com.example.prescription_generator.service.imp;

import com.example.prescription_generator.model.dto.DoctorDTO;
import com.example.prescription_generator.model.dto.UserDTO;
import com.example.prescription_generator.service.ValidationService;
import org.springframework.stereotype.Component;

@Component
public class ValidationServiceImp implements ValidationService {

    @Override
    public String validateDoctorDetails(UserDTO userDTO) {
        return "";
    }
}
