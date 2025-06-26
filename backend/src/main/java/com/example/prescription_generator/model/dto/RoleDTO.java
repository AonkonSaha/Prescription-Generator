package com.example.prescription_generator.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "Role", description = "Role DTO used for managing role-related operations")
public class RoleDTO {

    @Schema(description = "New role name to be created or updated", example = "ADMIN")
    private String newRoleName;

    @Schema(description = "Existing role name to update or reference", example = "USER")
    private String existingRoleName;

    @Schema(description = "Role name to be deleted", example = "GUEST")
    private String deletedRoleName;

    @Schema(description = "Short description of the role and its permissions", example = "Administrator with full access")
    private String description;

    @Schema(description = "Mobile number of the user associated with the role operation", example = "01881264859")
    private String mobileNumber;
}
