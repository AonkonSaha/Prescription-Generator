package com.example.prescription_generator.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginDTO {

   String mobileNumber;
   String userName;
   String password;
}
