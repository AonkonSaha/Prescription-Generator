package com.example.prescription_generator.controller;

import com.example.prescription_generator.exceptions.InvalidPrescriptionArgumentException;
import com.example.prescription_generator.exceptions.PrescriptionNotFoundException;
import com.example.prescription_generator.model.dto.PrescriptionDTO;
import com.example.prescription_generator.model.mapper.PrescriptionMapper;
import com.example.prescription_generator.service.PrescriptionPdfGeneratorService;
import com.example.prescription_generator.service.PrescriptionService;
import com.example.prescription_generator.service.PrescriptionValidationService;
import com.example.prescription_generator.service.ValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Prescription API", description = "Operations related to prescriptions")
@SecurityRequirement(name = "bearerAuth")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final PrescriptionMapper prescriptionMapper;
    private final ValidationService validationService;
    private final PrescriptionValidationService prescriptionValidationService;
    private final PrescriptionPdfGeneratorService prescriptionPdfGeneratorService;

    @Operation(summary = "Create a new prescription")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prescription created",
                    content = @Content(schema = @Schema(implementation = PrescriptionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/v1/register")
    public ResponseEntity<PrescriptionDTO> register(@RequestBody PrescriptionDTO prescriptionDTO) {
        if (!validationService.validatePrescriptionDetails(prescriptionDTO).isEmpty()) {
            throw new InvalidPrescriptionArgumentException(validationService.validatePrescriptionDetails(prescriptionDTO));
        }
        return ResponseEntity.ok(prescriptionMapper.toPrescriptionDTO(
                prescriptionService.savePrescription(prescriptionMapper.toPrescription(prescriptionDTO))));
    }

    @Operation(summary = "Update an existing prescription by ID")
    @PutMapping("/v1/update/{id}")
    public ResponseEntity<PrescriptionDTO> update(
            @RequestBody PrescriptionDTO prescriptionDTO,
            @Parameter(description = "ID of the prescription") @PathVariable("id") Long id) {
        if (!validationService.validatePrescriptionDetails(prescriptionDTO).isEmpty()) {
            throw new InvalidPrescriptionArgumentException(validationService.validatePrescriptionDetails(prescriptionDTO));
        }
        return ResponseEntity.ok(prescriptionMapper.toPrescriptionDTO(
                prescriptionService.updatePrescription(id, prescriptionDTO)));
    }

    @Operation(summary = "Delete a prescription by ID")
    @DeleteMapping("/v1/delete/{id}")
    public ResponseEntity<String> delete(
            @Parameter(description = "ID of the prescription") @PathVariable("id") Long id) {
        if (!prescriptionValidationService.isExitPrescriptionById(id)) {
            throw new PrescriptionNotFoundException("Prescription doesn't exist!");
        }
        prescriptionService.deletePrescription(id);
        return ResponseEntity.ok("Prescription deleted successfully!");
    }

    @Operation(summary = "Get all prescriptions for current doctor")
    @GetMapping("/v1/get/all")
    public ResponseEntity<Map<String, List<PrescriptionDTO>>> getPrescription() {
        String contact = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(Map.of("prescriptions", prescriptionMapper.toPrescriptionDTOS(
                prescriptionService.getPrescriptions(contact))));
    }

    @Operation(summary = "Get prescriptions from the most recent month")
    @GetMapping("/v1/get/all/recent/month")
    public ResponseEntity<Map<String, List<PrescriptionDTO>>> getRecentMonthPrescription() {
        String contact = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(Map.of("prescriptions", prescriptionMapper.toPrescriptionDTOS(
                prescriptionService.getPrescriptionsFromRecentMonth(contact))));
    }

    @Operation(summary = "Get prescriptions between two dates")
    @GetMapping("/v1/get/all/{startDate}/{endDate}")
    public ResponseEntity<Map<String, List<PrescriptionDTO>>> getDateRangePrescription(
            @Parameter(description = "Start date in yyyy-MM-dd format") @PathVariable("startDate") LocalDate startDate,
            @Parameter(description = "End date in yyyy-MM-dd format") @PathVariable("endDate") LocalDate endDate) {
        String contact = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(Map.of("prescriptions", prescriptionMapper.toPrescriptionDTOS(
                prescriptionService.getPrescriptionsFromDateRange(startDate, endDate, contact))));
    }

    @Operation(summary = "Get prescription by ID")
    @GetMapping("/v1/get/{id}")
    public ResponseEntity<PrescriptionDTO> getPrescription(
            @Parameter(description = "Prescription ID") @PathVariable("id") Long id) {
        String contact = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(prescriptionMapper.toPrescriptionDTO(
                prescriptionService.getPrescriptionsById(id)));
    }

    @Operation(summary = "Get prescription report summary for doctor")
    @GetMapping("/v1/report")
    public ResponseEntity<?> getReport() {
        String contact = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(prescriptionMapper.toReportDTOS(
                prescriptionService.getReports(contact)));
    }

    @Operation(summary = "Generate PDF for a prescription by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF generated and returned as download",
                    content = @Content(mediaType = "application/pdf")),
            @ApiResponse(responseCode = "404", description = "Prescription not found",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/v1/pdf/generate/{id}")
    public ResponseEntity<byte[]> generatePrescription(
            @Parameter(description = "ID of the prescription to generate PDF for") @PathVariable("id") Long id) {
        if (!prescriptionValidationService.isExitPrescriptionById(id)) {
            throw new PrescriptionNotFoundException("Prescription doesn't exist!");
        }
        byte[] pdf = prescriptionPdfGeneratorService.pdfGenerator(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "prescription.pdf");
        return ResponseEntity.ok().headers(headers).body(pdf);
    }
}
