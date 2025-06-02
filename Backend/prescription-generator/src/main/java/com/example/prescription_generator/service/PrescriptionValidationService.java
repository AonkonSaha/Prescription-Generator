package com.example.prescription_generator.service;

import java.time.LocalDate;
import java.util.Set;


public interface PrescriptionValidationService {

    boolean isEmptyPrescriptionDate(LocalDate date);
    boolean isEmptyPatientName(String name);
    boolean isEmptyPatientAge(Integer age);
    boolean isEmptyPatientGender(String gender);
    boolean isEmptyDiagnosis(Set<String> diagnosis);
    boolean isEmptyMedicines(Set<String> medicines);
    boolean isEmptyNextVisitDate(LocalDate date);
    boolean isValidPrescriptionDate(LocalDate date);
    boolean isValidPatientAge(Integer age);
    boolean isValidPatientGender(String gender);
    boolean isValidNextVisitDate(LocalDate date);
    boolean isExitPrescriptionById(Long id);

}
