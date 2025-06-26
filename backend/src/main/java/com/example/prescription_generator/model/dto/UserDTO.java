package com.example.prescription_generator.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@Schema(name = "User", description = "User DTO containing profile and credential information")
public class UserDTO {

    @Schema(description = "Full name of the user",
            example = "Aonkon Saha",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;

    @Schema(description = "Unique mobile number of the user",
            example = "01881264859",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String mobileNumber;

    @Schema(description = "Gender of the user",
            example = "Male",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String gender;

    @Schema(description = "Email address of the user",
            example = "aonkon@gmail.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

    @Schema(description = "Password (hashed or plain depending on context)",
            example = "P@ssw0rd123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String password;

    @Schema(description = "Date of birth of the user in yyyy-MM-dd format",
            example = "1995-07-24",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @Schema(description = "URL to the profile picture", example = "https://example.com/images/profile.jpg")
    private String profilePictureUrl;

    @Schema(description = "Residential address", example = "123 Main Street, Dhaka")
    private String address;

    @Schema(description = "Designation/title",
            example = "Cardiologist",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String designation;

    @Schema(description = "Medical license number",
            example = "MD-5678-AB",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String licenseNumber;

    @Schema(description = "Total years of experience", example = "8")
    private Integer yearsOfExperience;

    @Schema(description = "Name of the hospital or clinic", example = "Dhaka Medical Hospital")
    private String hospitalOrClinicName;

    @Schema(description = "Languages the user can speak", example = "English, Bangla")
    private String languagesSpoken;

    @Schema(description = "Set of academic degrees",
            example = "[\"MBBS\", \"FCPS\"]",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Set<String> degrees = new HashSet<>();

    @Schema(description = "Availability status of the user",
            example = "true",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Boolean availabilityStatus;
}
