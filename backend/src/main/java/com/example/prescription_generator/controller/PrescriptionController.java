package com.example.prescription_generator.controller;

import com.example.prescription_generator.exceptions.InvalidPrescriptionException;
import com.example.prescription_generator.exceptions.PrescriptionNotFoundException;
import com.example.prescription_generator.model.dto.PrescriptionDTO;
import com.example.prescription_generator.model.mapper.PrescriptionMapper;
import com.example.prescription_generator.service.PrescriptionPdfGeneratorService;
import com.example.prescription_generator.service.PrescriptionService;
import com.example.prescription_generator.service.PrescriptionValidationService;
import com.example.prescription_generator.service.ValidationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prescription")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PrescriptionController {
    private final PrescriptionService prescriptionService;
    private final PrescriptionMapper prescriptionMapper;
    private final ValidationService validationService;
    private final PrescriptionValidationService prescriptionValidationService;
    private final PrescriptionPdfGeneratorService prescriptionPdfGeneratorService;

    @PostMapping("/register")
    public ResponseEntity<PrescriptionDTO> register(@RequestBody PrescriptionDTO prescriptionDTO){
        if(!validationService.validatePrescriptionDetails(prescriptionDTO).isEmpty()){
            throw new InvalidPrescriptionException(validationService.validatePrescriptionDetails(prescriptionDTO));
        }
        return ResponseEntity.ok(prescriptionMapper.toPrescriptionDTO(
                prescriptionService.savePrescription(prescriptionMapper.toPrescription(prescriptionDTO))));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<PrescriptionDTO> update(@RequestBody PrescriptionDTO prescriptionDTO, @PathVariable("id") Long id){
        if(!validationService.validatePrescriptionDetails(prescriptionDTO).isEmpty()){
            throw new InvalidPrescriptionException(validationService.validatePrescriptionDetails(prescriptionDTO));
        }
        return ResponseEntity.ok(prescriptionMapper.toPrescriptionDTO(
                prescriptionService.updatePrescription(id,prescriptionDTO)
        ));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id){
        if (!prescriptionValidationService.isExitPrescriptionById(id)){
            throw new PrescriptionNotFoundException("Prescription doesn't exit!");
        }
        prescriptionService.deletePrescription(id);
        return ResponseEntity.ok("Prescription deleted successfully!");
    }
    @GetMapping("/get/all")
    public ResponseEntity<Map<String, List<PrescriptionDTO>>> getPrescription(){
        String contact= SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(Map.of("prescriptions",prescriptionMapper.toPrescriptionDTOS(
                prescriptionService.getPrescriptions(contact))));
    }
    @GetMapping("/get/all/recent/month")
    public ResponseEntity<Map<String, List<PrescriptionDTO>>> getRecentMonthPrescription(){
        String contact= SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(Map.of("prescriptions",prescriptionMapper.toPrescriptionDTOS(
                prescriptionService.getPrescriptionsFromRecentMonth(contact))));
    }

    @GetMapping("/get/all/{startDate}/{endDate}")
    public ResponseEntity<Map<String, List<PrescriptionDTO>>> getDateRangePrescription(
            @PathVariable("startDate") LocalDate startDate, @PathVariable("endDate") LocalDate endDate){
        String contact= SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(Map.of("prescriptions",prescriptionMapper.toPrescriptionDTOS(
                prescriptionService.getPrescriptionsFromDateRange(startDate,endDate,contact))));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getPrescription(@PathVariable("id") Long id){
        String contact= SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(prescriptionMapper.toPrescriptionDTO(prescriptionService.getPrescriptionsById(id)));
    }
    @GetMapping("/report")
    public ResponseEntity<?> getReport(){
       String contact= SecurityContextHolder.getContext().getAuthentication().getName();
       return ResponseEntity.ok(prescriptionMapper.toReportDTOS(prescriptionService.getReports(contact)));
    }

    @GetMapping("/pdf/generate/{id}")
    public ResponseEntity<?> generatePrescription(@PathVariable("id") Long id){
        if (!prescriptionValidationService.isExitPrescriptionById(id)){
            throw new PrescriptionNotFoundException("Prescription doesn't exit!");
        }
        byte[] pdf = prescriptionPdfGeneratorService.pdfGenerator(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "prescription.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }
}
