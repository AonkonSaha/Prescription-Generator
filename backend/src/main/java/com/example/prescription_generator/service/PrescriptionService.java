package com.example.prescription_generator.service;

import com.example.prescription_generator.model.dto.PrescriptionDTO;
import com.example.prescription_generator.model.dto.ReportDTO;
import com.example.prescription_generator.model.entity.Prescription;
import com.example.prescription_generator.model.entity.Report;

import java.time.LocalDate;
import java.util.List;

public interface PrescriptionService {

    Prescription savePrescription(Prescription prescription);
    Prescription updatePrescription(Long id, PrescriptionDTO prescriptionDTO);

    void deletePrescription(Long id);

    List<Prescription> getPrescriptions(String contact);

    List<Report> getReports(String contact);

    Prescription getPrescriptionsById(Long id);

    List<Prescription> getPrescriptionsFromRecentMonth(String contact);

    List<Prescription> getPrescriptionsFromDateRange(LocalDate startDate, LocalDate endDate, String contact);
}
