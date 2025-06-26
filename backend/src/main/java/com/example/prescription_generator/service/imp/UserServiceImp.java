package com.example.prescription_generator.service.imp;

import com.example.prescription_generator.exceptions.UserNotFoundException;
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

import java.util.List;
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
                new UsernamePasswordAuthenticationToken(loginDTO.getMobileNumber(),loginDTO.getPassword()));
        String token=jwtUtils.generateToken(user.getName(),loginDTO.getMobileNumber());
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
        MUser user=findUserByContact(contact);
        if(user==null){
            throw new UserNotFoundException("User not found");
        }
        user.setIsActive(false);
        saveUser(user);
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Override
    public MUser updatePatientWithOutPassword(String fullName, String email, String dob, String gender, MultipartFile photo) {
        return null;
    }

    @Override
    public MUser findUserByContact(String contact) {
        Optional<MUser> mUser=userRepo.findByContact(contact);
        if(mUser.isEmpty()) {
           throw new UserNotFoundException("User not found");
        }
        return mUser.get();
    }

    @Override
    public void deleteUser(String contact) {
        Optional<MUser> mUser=userRepo.findByContact(contact);
        if(mUser.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        userRepo.delete(mUser.get());
    }

    @Override
    public List<MUser> findAllUsers() {
        List<MUser> users=userRepo.findAll();
        if(users.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        return users;
    }
}
