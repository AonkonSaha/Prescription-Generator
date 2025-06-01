package com.example.prescription_generator.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class PrescriptionDTO {

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate prescriptionDate;
    @NotNull
    private String patientName;
    @NotNull
    private Integer patientAge;
    @NotNull
    private String patientGender;
    @NotNull
    private Set<String> diagnosis=new HashSet<>();
    @NotNull
    private Set<String> medicines=new HashSet<>();

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate nextVisitDate;


}
