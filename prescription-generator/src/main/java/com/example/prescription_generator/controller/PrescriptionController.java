package com.example.prescription_generator.controller;

import com.example.prescription_generator.model.dto.PrescriptionDTO;
import com.example.prescription_generator.model.entity.Prescription;
import com.example.prescription_generator.model.mapper.PrescriptionMapper;
import com.example.prescription_generator.service.PrescriptionService;
import com.example.prescription_generator.service.PrescriptionValidationService;
import com.example.prescription_generator.service.ValidationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody PrescriptionDTO prescriptionDTO){
        if(!validationService.validatePrescriptionDetails(prescriptionDTO).isEmpty()){
            return ResponseEntity.badRequest().body(validationService.validatePrescriptionDetails(prescriptionDTO));
        }
        return ResponseEntity.ok(prescriptionMapper.toPrescriptionDTO(
                prescriptionService.savePrescription(prescriptionMapper.toPrescription(prescriptionDTO))));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody PrescriptionDTO prescriptionDTO, @PathVariable("id") Long id){
        if(!validationService.validatePrescriptionDetails(prescriptionDTO).isEmpty()){
            return ResponseEntity.badRequest().body(validationService.validatePrescriptionDetails(prescriptionDTO));
        }
        return ResponseEntity.ok(prescriptionMapper.toPrescriptionDTO(
                prescriptionService.updatePrescription(id,prescriptionDTO)
        ));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        if (!prescriptionValidationService.isExitPrescriptionById(id)){
            return ResponseEntity.badRequest().body("Prescription doesn't exit!");
        }
        prescriptionService.deletePrescription(id);
        return ResponseEntity.ok("Prescription deleted successfully!");
    }
    @GetMapping("/prescription")
    public ResponseEntity<Map<String, List<PrescriptionDTO>>> getPrescription(){
        String contact= SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(Map.of("prescriptions",prescriptionMapper.toPrescriptions(
                prescriptionService.getPrescriptions(contact))));
    }
    @GetMapping("/report")
    public ResponseEntity<?> getReport(){
       String contact= SecurityContextHolder.getContext().getAuthentication().getName();
       return ResponseEntity.ok(prescriptionMapper.toReportDTOS(prescriptionService.getReports(contact)));
    }


}
