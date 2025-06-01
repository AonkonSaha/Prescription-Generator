package com.example.prescription_generator.service.imp;

import com.example.prescription_generator.repository.PrescriptionRepo;
import com.example.prescription_generator.service.PrescriptionValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PrescriptionValidationServiceImp implements PrescriptionValidationService {
    private final PrescriptionRepo prescriptionRepo;
    @Override
    public boolean isEmptyPrescriptionDate(LocalDate date) {
        return date==null;
    }

    @Override
    public boolean isEmptyPatientName(String name) {
        return name.isEmpty() ;
    }

    @Override
    public boolean isEmptyPatientAge(Integer age) {
        return age.toString().isEmpty();
    }

    @Override
    public boolean isEmptyPatientGender(String gender) {
        return gender.isEmpty();
    }

    @Override
    public boolean isEmptyDiagnosis(Set<String> diagnosis) {
        return diagnosis.isEmpty();
    }

    @Override
    public boolean isEmptyMedicines(Set<String> medicines) {
        return medicines.isEmpty() ;
    }

    @Override
    public boolean isEmptyNextVisitDate(LocalDate date) {
        return date==null;
    }

    @Override
    public boolean isValidPrescriptionDate(LocalDate date) {
        return date.equals(LocalDate.now());
    }

    @Override
    public boolean isValidPatientAge(Integer age) {
        return age>=1 && age<=200;
    }

    @Override
    public boolean isValidPatientGender(String gender) {
        return gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female")
                || gender.equalsIgnoreCase("other");
    }

    @Override
    public boolean isValidNextVisitDate(LocalDate date) {
        return date.isAfter(LocalDate.now());
    }

    @Override
    public boolean isExitPrescriptionById(Long id) {
        return prescriptionRepo.existsById(id);
    }
}
