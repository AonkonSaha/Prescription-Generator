package com.example.prescription_generator.service.imp;

import com.example.prescription_generator.model.entity.MUser;
import com.example.prescription_generator.repository.UserRepo;
import com.example.prescription_generator.service.UserValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserValidationServiceImp implements UserValidationService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    @Override
    public boolean isExitUserPassword(String contact, String password){
        Optional<MUser> mUser=userRepo.findByContact(contact);
        if(mUser.isEmpty()){
            return false;
        }
        return passwordEncoder.matches(password,mUser.get().getPassword());
    }
}
