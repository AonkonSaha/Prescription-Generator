package com.example.prescription_generator.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Schema(name = "Prescription", description = "DTO representing a medical prescription with patient details, diagnosis, and treatment plan")
public class PrescriptionDTO {

    @Schema(
            description = "Unique identifier of the prescription. Optional during creation.",
            example = "123",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
            description = "Date when the prescription was issued",
            example = "2025-06-26",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate prescriptionDate;

    @Schema(
            description = "Name of the patient",
            example = "John Doe",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String patientName;

    @Schema(
            description = "Age of the patient in years",
            example = "45",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer patientAge;

    @Schema(
            description = "Gender of the patient (e.g., Male, Female, Other)",
            example = "Male",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String patientGender;

    @Schema(
            description = "Set of diagnosed conditions",
            example = "[\"Hypertension\", \"Diabetes\"]"
    )
    private Set<String> diagnosis = new HashSet<>();

    @Schema(
            description = "Set of prescribed medicines with dosage",
            example = "[\"Aspirin 100mg\", \"Metformin 500mg\"]"
    )
    private Set<String> medicines = new HashSet<>();

    @Schema(
            description = "Date of the next scheduled visit",
            example = "2025-07-10"
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate nextVisitDate;
}
