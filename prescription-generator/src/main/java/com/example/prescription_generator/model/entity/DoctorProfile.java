package com.example.prescription_generator.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "doctors")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DoctorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String doctorName;
    private String designation;
    private String licenseNumber;
    private Integer yearsOfExperience;
    private String hospitalOrClinicName;
    private String profilePictureUrl;
    private String languagesSpoken;
    @ElementCollection
    @CollectionTable(name = "doctor_degrees", joinColumns = @JoinColumn(name = "doctor_id"))
    @Column(name = "degree")
    private Set<String> degrees=new HashSet<>();
    private Double rating;
    private Boolean availabilityStatus;
    @OneToOne(fetch = FetchType.EAGER,cascade={CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    private MUser user;

    @OneToMany(mappedBy = "doctorProfile",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Prescription> prescriptions=new HashSet<>();

    @OneToMany(mappedBy = "doctorProfile",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Report> reports=new HashSet<>();
}
