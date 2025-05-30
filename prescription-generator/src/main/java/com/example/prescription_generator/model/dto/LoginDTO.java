package com.example.prescription_generator.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginDTO {

   @NotNull
   String contact;
   String userName;
   @NotNull
   String password;
}
