package com.example.prescription_generator.model.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reports")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name="report_day")
    private LocalDate day;

    @OneToMany(mappedBy = "report",fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Set<Prescription> prescriptions=new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER,cascade={CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "doctor_id")
    private DoctorProfile doctorProfile;
}
