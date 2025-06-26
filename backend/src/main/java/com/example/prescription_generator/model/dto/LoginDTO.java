package com.example.prescription_generator.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "Login", description = "DTO for user login containing credentials")
public class LoginDTO {

   @Schema(
           description = "Registered mobile number of the user",
           example = "01881264859",
           requiredMode = Schema.RequiredMode.REQUIRED
   )
   private String mobileNumber;

   @Schema(
           description = "Username of the user (optional if mobile number is provided)",
           example = "aonkon.saha",
           requiredMode = Schema.RequiredMode.NOT_REQUIRED
   )
   private String userName;

   @Schema(
           description = "User account password",
           example = "YourSecurePassword123!",
           requiredMode = Schema.RequiredMode.REQUIRED
   )
   private String password;
}
