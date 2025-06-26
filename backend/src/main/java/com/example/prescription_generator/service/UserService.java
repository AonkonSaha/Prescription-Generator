package com.example.prescription_generator.service;

import com.example.prescription_generator.model.dto.LoginDTO;
import com.example.prescription_generator.model.dto.PasswordDTO;
import com.example.prescription_generator.model.entity.MUser;
//import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    MUser saveUser(MUser user);
    String authenticateUser(MUser user, LoginDTO loginDTO);
    void  updateUserPassword(String contact, PasswordDTO passwordDTO);
    void logoutUser(String contact);
    MUser updatePatientWithOutPassword(String fullName, String email, String dob, String gender, MultipartFile photo);
    MUser findUserByContact(String contact);
    void deleteUser(String contact);
    List<MUser> findAllUsers();
}
