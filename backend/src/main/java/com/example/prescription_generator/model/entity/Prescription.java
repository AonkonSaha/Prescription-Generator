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
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate prescriptionDate;
    @NotNull
    @Column(nullable = false)
    private String patientName;
    @NotNull
    @Column(nullable = false)
    private Integer patientAge;
    @NotNull
    @Column(nullable = false)
    private String patientGender;
    private Set<String> diagnosis=new HashSet<>();
    private Set<String> medicines=new HashSet<>();
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate nextVisitDate;

    @ManyToOne(fetch = FetchType.EAGER,cascade={CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "doctor_id")
    private DoctorProfile doctorProfile;

    @ManyToOne(fetch = FetchType.EAGER,cascade={CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "report_id")
    private Report report;


}
