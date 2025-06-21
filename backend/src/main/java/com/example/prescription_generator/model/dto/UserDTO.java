package com.example.prescription_generator.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
public class UserDTO {

    private String name;
    private String mobileNumber;
    private String gender;
    private String email;
    private String password;
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String profilePictureUrl;
    private String address;
    private String profession;
    private String designation;
    private String licenseNumber;
    private Integer yearsOfExperience;
    private String hospitalOrClinicName;
    private String languagesSpoken;
    private Set<String> degrees=new HashSet<>();
    private Double rating;
    private Boolean availabilityStatus;
}
