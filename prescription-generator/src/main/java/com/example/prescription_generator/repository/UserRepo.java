package com.example.prescription_generator.repository;

import com.example.prescription_generator.model.entity.MUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<MUser, Long> {
    Optional<MUser> findByContact(String contact);
}
