package com.example.prescription_generator.service.imp;

import com.example.prescription_generator.jwt.utils.JwtUtils;
import com.example.prescription_generator.model.dto.LoginDTO;
import com.example.prescription_generator.model.dto.PasswordDTO;
import com.example.prescription_generator.model.entity.MUser;
import com.example.prescription_generator.repository.UserRepo;
import com.example.prescription_generator.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserServiceImp implements UserService {
    private final UserRepo userRepo;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    @Override
    public MUser saveUser(MUser user) {
        userRepo.save(user);
        return user;
    }

    @Override
    public String authenticateUser(MUser user, LoginDTO loginDTO) {
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getContact(),loginDTO.getPassword()));
        String token=jwtUtils.generateToken(user.getName(),loginDTO.getContact());
        user.setIsActive(true);
        saveUser(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return token;
    }

    @Override
    public void updateUserPassword(String contact, PasswordDTO passwordDTO) {

    }

    @Override
    public void logoutUser(String contact) {

    }

    @Override
    public MUser updatePatientWithOutPassword(String fullName, String email, String dob, String gender, MultipartFile photo) {
        return null;
    }

    @Override
    public MUser findUserByContact(String contact) {
        Optional<MUser> mUser=userRepo.findByContact(contact);
        if(mUser.isEmpty()) {
            return null;
        }
        return mUser.get();
    }
}
