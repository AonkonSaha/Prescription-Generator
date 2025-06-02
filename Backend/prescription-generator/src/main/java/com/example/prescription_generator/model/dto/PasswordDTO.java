package com.example.prescription_generator.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PasswordDTO {

    private String password;
    private String newPassword;
    private String confirmPassword;
}
