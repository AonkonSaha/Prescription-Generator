package com.example.prescription_generator.service.imp;

import com.example.prescription_generator.model.dto.PrescriptionDTO;
import com.example.prescription_generator.model.dto.UserDTO;
import com.example.prescription_generator.service.PrescriptionValidationService;
import com.example.prescription_generator.service.UserValidationService;
import com.example.prescription_generator.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidationServiceImp implements ValidationService {
    private final UserValidationService userValidationService;
    private final PrescriptionValidationService prescriptionValidationService;
    @Override
    public String validateDoctorDetails(UserDTO userDTO) {
        if(userValidationService.isEmptyUserName(userDTO.getName())){
            return "Name can't be empty!";
        }
        if(userValidationService.isExitUserByContact(userDTO.getMobileNumber())){
            return "This Contact Number is already exit!";
        }
        if(userValidationService.isEmptyUserContact(userDTO.getMobileNumber())){
            return "Mobile Number can't be empty!";
        }
        if(!userValidationService.isValidUserContactLength(userDTO.getMobileNumber())){
            return "Mobile Number must be 11 digits!";
        }
        if(!userValidationService.isValidUserContactDigit(userDTO.getMobileNumber())){
            return "Mobile Number must be contain only digits!";
        }
        if(!userValidationService.isValidUserPasswordLength(userDTO.getPassword())){
            return "Password must be at least 8 characters long!";
        }
        if(!userDTO.getEmail().isEmpty() && !userValidationService.isValidEmailFormat(userDTO.getEmail())){

            return "Email isn't valid!";
        }
        if(!userValidationService.isValidGender(userDTO.getGender())){
            return "Gender must be male,female or other!";
        }
        if(userValidationService.isEmptyDoctorDesignation(userDTO.getDesignation())){
            return "Designation can't be empty!";
        }
        if(userValidationService.isEmptyDoctorDegrees(userDTO.getDegrees())){
            return "Degrees as Array can't be empty!";
        }
        if(userValidationService.isEmptyLincenseNumber(userDTO.getLicenseNumber())){
            return "License Number can't be empty!";
        }
        if(!userValidationService.isValidDoctorAges(userDTO.getDateOfBirth())){
            return "Date of Birth must be 25 to 120 years old!";
        }
        return "";
    }

    @Override
    public String validatePrescriptionDetails(PrescriptionDTO prescriptionDTO) {
        if (prescriptionValidationService.isEmptyPrescriptionDate(prescriptionDTO.getPrescriptionDate()) ) {
            return "Prescription Date can't be empty!";
        }
        if (prescriptionValidationService.isEmptyPatientName(prescriptionDTO.getPatientName()) ) {
            return "Patient Name can't be empty!";
        }
        if (prescriptionValidationService.isEmptyPatientAge(prescriptionDTO.getPatientAge()) ) {
            return "Patient Age can't be empty!";
        }
        if (prescriptionValidationService.isEmptyPatientGender(prescriptionDTO.getPatientGender()) ) {
            return "Patient Gender can't be empty!";
        }
        if (prescriptionValidationService.isEmptyDiagnosis(prescriptionDTO.getDiagnosis()) ) {
            return "Diagnosis can't be empty!";
        }
        if (prescriptionValidationService.isEmptyMedicines(prescriptionDTO.getMedicines()) ) {
            return "Medicines can't be empty!";
        }
        if (prescriptionValidationService.isEmptyNextVisitDate(prescriptionDTO.getNextVisitDate()) ) {
            return "Next Visit Date can't be empty!";
        }
//        if (!prescriptionValidationService.isValidPrescriptionDate(prescriptionDTO.getPrescriptionDate()) ) {
//            return "Prescription Date must be in Today!";
//        }
        if (!prescriptionValidationService.isValidPatientAge(prescriptionDTO.getPatientAge()) ) {
            return "Patient Age must be between 1 and 200!";
        }
        if (!prescriptionValidationService.isValidPatientGender(prescriptionDTO.getPatientGender()) ) {
            return "Patient Gender must be male,female or other!";
        }
        if (!prescriptionValidationService.isValidNextVisitDate(prescriptionDTO.getPrescriptionDate(),prescriptionDTO.getNextVisitDate()) ) {
            return "Next Visit Date must be in after the Prescription Date!";
        }
        return "";
    }
}
