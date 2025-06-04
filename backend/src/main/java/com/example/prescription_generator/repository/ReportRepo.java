package com.example.prescription_generator.repository;

import com.example.prescription_generator.model.entity.DoctorProfile;
import com.example.prescription_generator.model.entity.Report;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReportRepo extends JpaRepository<Report, Long> {
    Optional<Report> findByDay(LocalDate day);

    @Query("SELECT r FROM Report r WHERE r.doctorProfile.user.contact = :contact")
    List<Report> findAllByUserContact(@Param("contact") String contact);

    @Query("SELECT r FROM Report r WHERE r.doctorProfile.id = :doctorId AND r.day = :day")
    Optional<Report> findByDoctorIdAndDay(@Param("doctorId") Long doctorId, @Param("day") LocalDate day);
}
