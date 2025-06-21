package com.example.prescription_generator.controller;


import com.example.prescription_generator.exceptions.InvalidLoginException;
import com.example.prescription_generator.exceptions.UserNotFoundException;
import com.example.prescription_generator.exceptions.InvalidUserException;
import com.example.prescription_generator.jwt.utils.JwtUtils;
import com.example.prescription_generator.jwt.utils.RequestUtils;
import com.example.prescription_generator.model.dto.LoginDTO;
import com.example.prescription_generator.model.dto.UserDTO;
import com.example.prescription_generator.model.entity.MUser;
import com.example.prescription_generator.model.mapper.UserMapper;
import com.example.prescription_generator.service.UserService;
import com.example.prescription_generator.service.UserValidationService;
import com.example.prescription_generator.service.ValidationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;
    private final ValidationService validationService;
    private final UserValidationService userValidationService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) {
        if(!validationService.validateDoctorDetails(userDTO).isEmpty()){
            throw new InvalidUserException(validationService.validateDoctorDetails(userDTO));
        }
        return ResponseEntity.ok(userMapper.toUserDTO(
                userService.saveUser(userMapper.toUser(userDTO))));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody LoginDTO loginDTO) {
        System.out.println("Contact: "+loginDTO.getMobileNumber()+" Password: "+loginDTO.getPassword());
        MUser user= userService.findUserByContact(loginDTO.getMobileNumber());
        if(user==null){
            throw new UserNotFoundException("Mobile Number doesn't exit!");
        }
        if(!userValidationService.isExitUserPassword(loginDTO.getMobileNumber(),loginDTO.getPassword())){
            throw new InvalidLoginException("Password is incorrect!");
        }
        return ResponseEntity.ok(Map.of("token",userService.authenticateUser(user,loginDTO)));
    }
    @PostMapping("/logout")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> logout(){
        HttpServletRequest request= RequestUtils.getCurrentHttpRequest();
        if(request==null){
            return ResponseEntity.badRequest().body("HttpServletRequest Object is empty");
        }
        String token = request.getHeader("Authorization").substring(7);
        String contact=jwtUtils.extractContact(token);
        userService.logoutUser(contact);
        return ResponseEntity.ok("Logout successfully");
    }
}
