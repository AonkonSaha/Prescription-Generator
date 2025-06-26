package com.example.prescription_generator.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Report", description = "Report DTO representing daily counts")
public class ReportDTO {

    @Schema(description = "Report date", example = "2025-06-25")
    private LocalDate day;

    @Schema(description = "Total count for the day", example = "15")
    private Long count;
}
