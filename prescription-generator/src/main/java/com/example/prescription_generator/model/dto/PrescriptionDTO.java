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
    private LocalDate prescriptionDate;
    private String patientName;
    private Integer patientAge;
    private String patientGender;
    private Set<String> diagnosis=new HashSet<>();
    private Set<String> medicines=new HashSet<>();
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate nextVisitDate;


}
