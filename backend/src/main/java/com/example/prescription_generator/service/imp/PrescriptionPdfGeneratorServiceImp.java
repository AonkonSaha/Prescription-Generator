package com.example.prescription_generator.service.imp;

import com.example.prescription_generator.model.entity.Prescription;
import com.example.prescription_generator.repository.PrescriptionRepo;
import com.example.prescription_generator.service.PrescriptionPdfGeneratorService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrescriptionPdfGeneratorServiceImp implements PrescriptionPdfGeneratorService {
    private final PrescriptionRepo prescriptionRepo;

    @Override
    public byte[] pdfGenerator(Long id) {
        Prescription prescription = prescriptionRepo.findById(id).orElseThrow(() -> new RuntimeException("Prescription not found"));

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 50, 50, 50, 60);
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            addWatermark(writer);

            // Header with Logo
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{1f, 3f});

            URL logoUrl = getClass().getClassLoader().getResource("static/logo.jpg");
            if (logoUrl != null) {
                Image logo = Image.getInstance(logoUrl);
                logo.scaleToFit(60, 60);
                PdfPCell logoCell = new PdfPCell(logo);
                logoCell.setBorder(Rectangle.NO_BORDER);
                headerTable.addCell(logoCell);
            } else {
                headerTable.addCell("");
            }

            Font clinicFont = new Font(Font.HELVETICA, 16, Font.BOLD, Color.BLACK);
            Paragraph clinicName = new Paragraph("CMED Health\n", clinicFont);
            clinicName.add(new Chunk("31 DOHS Mohakhali, Dhaka • +880123456789\n", new Font(Font.HELVETICA, 10)));
            PdfPCell clinicCell = new PdfPCell(clinicName);
            clinicCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            clinicCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(clinicCell);
            document.add(headerTable);

            document.add(new LineSeparator());
            document.add(new Paragraph(" "));

            // Title
            Paragraph title = new Paragraph("Medical Prescription", new Font(Font.HELVETICA, 20, Font.BOLD, Color.DARK_GRAY));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Fonts
            Font labelFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font valueFont = new Font(Font.HELVETICA, 12);

            // Date
            document.add(new Paragraph("Date: " + new Date(), valueFont));

            // Doctor info with designation and degrees
            String doctorName = prescription.getDoctorProfile().getDoctorName();
            String designation = prescription.getDoctorProfile().getDesignation();
            String degrees = prescription.getDoctorProfile().getDegrees().stream().map(Object::toString).collect(Collectors.joining(", "));

            Paragraph doctorDetails = new Paragraph();
            doctorDetails.add(new Chunk("Doctor: ", labelFont));
            doctorDetails.add(new Chunk(doctorName + "\n", valueFont));
            if (designation != null && !designation.isEmpty())
                doctorDetails.add(new Chunk(designation + "\n", new Font(Font.HELVETICA, 11, Font.ITALIC, Color.DARK_GRAY)));
            if (!degrees.isEmpty())
                doctorDetails.add(new Chunk(degrees, new Font(Font.HELVETICA, 11, Font.NORMAL, Color.DARK_GRAY)));
            document.add(doctorDetails);

            document.add(new Paragraph("Patient: " + prescription.getPatientName(), valueFont));
            document.add(new Paragraph(" "));

            // Diagnosis
            document.add(new Paragraph("Diagnosis", labelFont));
            for (String diagnosis : prescription.getDiagnosis()) {
                document.add(new Paragraph("- " + diagnosis, valueFont));
            }
            document.add(new Paragraph(" "));

            // Medicines Table
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setWidths(new float[]{2f, 3f});

            table.addCell(getHeaderCell("Medicine"));
            table.addCell(getHeaderCell("Instructions"));

            for (String med : prescription.getMedicines()) {
                table.addCell(getValueCell(med.trim()));
                table.addCell(getValueCell("Take as prescribed"));
            }
            document.add(table);

            // QR Code
            document.add(new Paragraph(" "));
            Image qrImage = generateQrCodeImage("https://example.com/prescription/view?patient=" + prescription.getPatientName());
            qrImage.setAlignment(Image.ALIGN_RIGHT);
            qrImage.scalePercent(30);
            document.add(qrImage);

            // Signature
            document.add(new Paragraph(" "));
            document.add(new LineSeparator());
            Paragraph sig = new Paragraph("Doctor's Signature", new Font(Font.HELVETICA, 11, Font.ITALIC));
            sig.setAlignment(Element.ALIGN_RIGHT);
            document.add(sig);

            addFooter(document, writer);
            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating modern PDF", e);
        }
    }

    private static void addFooter(Document document, PdfWriter writer) {
        PdfContentByte cb = writer.getDirectContent();
        try {
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            cb.beginText();
            cb.setFontAndSize(bf, 9);
            cb.setColorFill(new Color(100, 100, 100));
            cb.showTextAligned(Element.ALIGN_CENTER,
                    "CMED Health | Confidential • www.cmed.com • +880123456789",
                    (document.right() + document.left()) / 2,
                    document.bottom() - 20,
                    0);
            cb.endText();
        } catch (Exception e) {
            throw new RuntimeException("Error adding footer", e);
        }
    }

    private static void addWatermark(PdfWriter writer) {
        PdfContentByte under = writer.getDirectContentUnder();
        try {
            BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            under.saveState();
            PdfGState gs1 = new PdfGState();
            gs1.setFillOpacity(0.08f);
            under.setGState(gs1);
            under.beginText();
            under.setFontAndSize(baseFont, 60);
            under.setColorFill(new Color(113, 109, 109));
            under.showTextAligned(Element.ALIGN_CENTER, "CMED Health",
                    297.5f, 421, 45);
            under.endText();
            under.restoreState();
        } catch (Exception e) {
            throw new RuntimeException("Error adding watermark", e);
        }
    }

    private static PdfPCell getHeaderCell(String text) {
        Font font = new Font(Font.HELVETICA, 12, Font.BOLD, Color.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(new Color(0, 121, 191));
        cell.setPadding(5);
        return cell;
    }

    private static PdfPCell getValueCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, new Font(Font.HELVETICA, 11)));
        cell.setPadding(5);
        return cell;
    }

    private static Image generateQrCodeImage(String text) throws WriterException, IOException, BadElementException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BufferedImage qrImage = com.google.zxing.client.j2se.MatrixToImageWriter.toBufferedImage(
                qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 150, 150)
        );
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "png", baos);
        return Image.getInstance(baos.toByteArray());
    }
}
