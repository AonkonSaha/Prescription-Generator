package com.example.prescription_generator.service;


public interface PrescriptionPdfGeneratorService {
    public byte[] pdfGenerator(Long id);
}
