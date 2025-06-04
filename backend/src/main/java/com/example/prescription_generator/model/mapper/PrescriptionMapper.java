package com.example.prescription_generator.model.mapper;

import com.example.prescription_generator.model.dto.PrescriptionDTO;
import com.example.prescription_generator.model.dto.ReportDTO;
import com.example.prescription_generator.model.entity.DoctorProfile;
import com.example.prescription_generator.model.entity.MUser;
import com.example.prescription_generator.model.entity.Prescription;
import com.example.prescription_generator.model.entity.Report;
import com.example.prescription_generator.repository.ReportRepo;
import com.example.prescription_generator.repository.UserRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
@Component
public class PrescriptionMapper {
    private final UserRepo userRepo;
    private final ReportRepo reportRepo;
    public Prescription toPrescription(PrescriptionDTO prescriptionDTO) {
        String contact= SecurityContextHolder.getContext().getAuthentication().getName();
         DoctorProfile doctorProfile = userRepo.findByContact(contact).get().getDoctorProfile();
        Prescription prescription= Prescription.builder()
                .prescriptionDate(prescriptionDTO.getPrescriptionDate())
                .patientName(prescriptionDTO.getPatientName())
                .patientAge(prescriptionDTO.getPatientAge())
                .patientGender(prescriptionDTO.getPatientGender())
                .diagnosis(prescriptionDTO.getDiagnosis())
                .medicines(prescriptionDTO.getMedicines())
                .nextVisitDate(prescriptionDTO.getNextVisitDate())
                .doctorProfile(doctorProfile)
                .month(prescriptionDTO.getPrescriptionDate().getMonthValue())
                .year(prescriptionDTO.getPrescriptionDate().getYear())
                .build();
        doctorProfile.getPrescriptions().add(prescription);

        Report report = reportRepo.findByDoctorIdAndDay(doctorProfile.getId(), prescriptionDTO.getPrescriptionDate())
                .orElseGet(() -> {
                    Report newReport = new Report();
                    newReport.setDay(prescriptionDTO.getPrescriptionDate());
                    newReport.setDoctorProfile(doctorProfile);
                    return newReport;
                });

        report.getPrescriptions().add(prescription);
        doctorProfile.getReports().add(report);
        prescription.setReport(report);
        return prescription;
    }

    public PrescriptionDTO toPrescriptionDTO(Prescription prescription) {
        return PrescriptionDTO.builder()
                .prescriptionDate(prescription.getPrescriptionDate())
                .patientName(prescription.getPatientName())
                .id(prescription.getId())
                .patientAge(prescription.getPatientAge())
                .patientGender(prescription.getPatientGender())
                .diagnosis(prescription.getDiagnosis())
                .medicines(prescription.getMedicines())
                .nextVisitDate(prescription.getNextVisitDate())
                .build();
    }

    public List<PrescriptionDTO> toPrescriptionDTOS(List<Prescription> prescriptions) {
        List<PrescriptionDTO> prescriptionDTOS=new ArrayList<>();
        for(Prescription prescription:prescriptions) {
            prescriptionDTOS.add(toPrescriptionDTO(prescription));
        }
        return prescriptionDTOS;
    }

    public List<ReportDTO> toReportDTOS(List<Report> reports) {
        List<ReportDTO> reportDTOS=new ArrayList<>();
        for (Report report:reports) {
            ReportDTO reportDTO=new ReportDTO();
            reportDTO.setCount((long) report.getPrescriptions().size());
            reportDTO.setDay(report.getDay());
            reportDTOS.add(reportDTO);
        }
        return reportDTOS;
    }
}
