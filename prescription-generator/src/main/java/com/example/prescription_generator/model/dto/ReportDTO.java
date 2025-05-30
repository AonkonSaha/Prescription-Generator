package com.example.prescription_generator.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReportDTO {
    private LocalDate day;
    private Long count;
}
