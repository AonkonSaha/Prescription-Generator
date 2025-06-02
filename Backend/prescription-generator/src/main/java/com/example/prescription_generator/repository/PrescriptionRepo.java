package com.example.prescription_generator.repository;

import com.example.prescription_generator.model.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepo extends JpaRepository <Prescription, Long> {

    @Query("SELECT p FROM Prescription p WHERE p.doctorProfile.user.contact = :contact")
    List<Prescription> findAllByDoctorContact(@Param("contact") String contact);
}
