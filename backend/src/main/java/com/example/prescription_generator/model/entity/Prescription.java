package com.example.prescription_generator.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "prescriptions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "prescription_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate prescriptionDate;
    @NotNull
    @Column(name="patient_name",nullable = false)
    private String patientName;
    @NotNull
    @Column(name="patient_age",nullable = false)
    private Integer patientAge;
    @NotNull
    @Column(name="patient_gender",nullable = false)
    private String patientGender;

    @ElementCollection
    @CollectionTable(name = "prescription_diagnosis", joinColumns = @JoinColumn(name = "prescription_id"))
    @Column(name = "diagnosis")
    private Set<String> diagnosis=new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "prescription_medicines", joinColumns = @JoinColumn(name = "prescription_id"))
    @Column(name = "medicine")
    private Set<String> medicines=new HashSet<>();

    @Column(name="next_visit_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate nextVisitDate;
    @Column(name="prescription_month")
    @NotNull
    private int month;
    @Column(name="prescription_year")
    @NotNull
    private int year;

    @ManyToOne(fetch = FetchType.EAGER,cascade={CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "doctor_id")
    private DoctorProfile doctorProfile;

    @ManyToOne(fetch = FetchType.EAGER,cascade={CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "report_id")
    private Report report;


}
