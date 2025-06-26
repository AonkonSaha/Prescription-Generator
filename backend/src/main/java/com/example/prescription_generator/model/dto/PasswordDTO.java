package com.example.prescription_generator.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "DTO for changing user password")
public class PasswordDTO {

    @Schema(
            description = "Current user password",
            example = "OldPassword@123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String password;

    @Schema(
            description = "New password to be set",
            example = "NewPassword@456",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String newPassword;

    @Schema(
            description = "Confirmation of the new password",
            example = "NewPassword@456",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String confirmPassword;
}
