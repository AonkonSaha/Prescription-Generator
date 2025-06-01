package com.example.prescription_generator.controller;


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
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;
    private final ValidationService validationService;
    private final UserValidationService userValidationService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        if(!validationService.validateDoctorDetails(userDTO).isEmpty()){
            return ResponseEntity.badRequest().body(validationService.validateDoctorDetails(userDTO));
        }
        return ResponseEntity.ok(userMapper.toUserDTO(
                userService.saveUser(userMapper.toUser(userDTO))));
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        System.out.println("Contact: "+loginDTO.getContact()+" Password: "+loginDTO.getPassword());
        MUser user= userService.findUserByContact(loginDTO.getContact());
        if(user==null){
            return ResponseEntity.badRequest().body("Mobile Number doesn't exit!");
        }
        if(!userValidationService.isExitUserPassword(loginDTO.getContact(),loginDTO.getPassword())){
            return ResponseEntity.badRequest().body("Password is incorrect!");
        }
        return ResponseEntity.ok(Map.of("token",userService.authenticateUser(user,loginDTO)));
    }
    @PostMapping("/logout")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> logout(){
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
