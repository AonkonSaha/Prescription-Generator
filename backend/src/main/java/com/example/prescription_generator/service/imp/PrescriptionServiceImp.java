package com.example.prescription_generator.service.imp;

import com.example.prescription_generator.model.dto.PrescriptionDTO;
import com.example.prescription_generator.model.entity.Prescription;
import com.example.prescription_generator.model.entity.Report;
import com.example.prescription_generator.repository.PrescriptionRepo;
import com.example.prescription_generator.repository.ReportRepo;
import com.example.prescription_generator.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImp implements PrescriptionService {
    private final PrescriptionRepo prescriptionRepo;
    private final ReportRepo reportRepo;
    @Override
    public Prescription savePrescription(Prescription prescription) {
        return prescriptionRepo.save(prescription);
    }

    @Override
    public Prescription updatePrescription(Long id, PrescriptionDTO prescriptionDTO) {
        Prescription prescription=prescriptionRepo.findById(id).get();
        prescription.setPrescriptionDate(prescriptionDTO.getPrescriptionDate());
        prescription.setPatientName(prescriptionDTO.getPatientName());
        prescription.setPatientAge(prescriptionDTO.getPatientAge());
        prescription.setPatientGender(prescriptionDTO.getPatientGender());
        prescription.setDiagnosis(prescriptionDTO.getDiagnosis());
        prescription.setMedicines(prescriptionDTO.getMedicines());
        prescription.setNextVisitDate(prescriptionDTO.getNextVisitDate());
        return prescriptionRepo.save(prescription);
    }

    @Override
    public void deletePrescription(Long id) {
        prescriptionRepo.deleteById(id);
    }

    @Override
    public List<Prescription> getPrescriptions(String contact) {
        return prescriptionRepo.findAllByDoctorContact(contact);
    }

    @Override
    public List<Report> getReports(String contact) {
        return reportRepo.findAllByUserContact(contact);
    }

    @Override
    public Prescription getPrescriptionsById(Long id) {
        Optional<Prescription> prescription= prescriptionRepo.findById(id);
        if(prescription.isEmpty()){
            return null;
        }
        return prescription.get();
    }
}
