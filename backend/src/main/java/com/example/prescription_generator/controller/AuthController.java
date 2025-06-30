package com.example.prescription_generator.controller;

import com.example.prescription_generator.exceptions.InvalidLoginArgumentException;
import com.example.prescription_generator.exceptions.UserNotFoundException;
import com.example.prescription_generator.exceptions.InvalidUserArgumentException;
import com.example.prescription_generator.jwt.utils.JwtUtils;
import com.example.prescription_generator.jwt.utils.RequestUtils;
import com.example.prescription_generator.model.dto.LoginDTO;
import com.example.prescription_generator.model.dto.UserDTO;
import com.example.prescription_generator.model.entity.MUser;
import com.example.prescription_generator.model.mapper.UserMapper;
import com.example.prescription_generator.service.UserService;
import com.example.prescription_generator.service.UserValidationService;
import com.example.prescription_generator.service.ValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth API", description = "Operations related to user creation, login, and logout")
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;
    private final ValidationService validationService;
    private final UserValidationService userValidationService;

    @Operation(
            summary = "Register a new doctor",
            description = "Creates a new user with doctor role after validating input details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully registered",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user data",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/v1/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) {
        if (!validationService.validateDoctorDetails(userDTO).isEmpty()) {
            throw new InvalidUserArgumentException(validationService.validateDoctorDetails(userDTO));
        }
        return ResponseEntity.ok(userMapper.toUserDTO(
                userService.saveUser(userMapper.toUser(userDTO))));
    }

    @Operation(
            summary = "Login with mobile and password",
            description = "Authenticates user and returns JWT token if successful"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful; JWT returned",
                    content = @Content(schema = @Schema(example = "{\"token\":\"<JWT_TOKEN>\"}"))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/v1/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDTO loginDTO) {
        MUser user = userService.findUserByContact(loginDTO.getMobileNumber());
        if (user == null) {
            throw new InvalidLoginArgumentException("Mobile Number doesn't exist!");
        }
        if (!userValidationService.isExitUserPassword(loginDTO.getMobileNumber(), loginDTO.getPassword())) {
            throw new InvalidLoginArgumentException("Password is incorrect!");
        }
        return ResponseEntity.ok(Map.of("token", userService.authenticateUser(user, loginDTO)));
    }

    @Operation(
            summary = "Logout current user",
            description = "Invalidates JWT token and logs out the authenticated user",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful",
                    content = @Content(schema = @Schema(example = "Logout successfully"))),
            @ApiResponse(responseCode = "400", description = "Missing or invalid request",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/v1/logout")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> logout() {
        HttpServletRequest request = RequestUtils.getCurrentHttpRequest();
        if (request == null) {
            return ResponseEntity.badRequest().body("HttpServletRequest Object is empty");
        }
        String token = request.getHeader("Authorization").substring(7);
        String contact = jwtUtils.extractContact(token);
        userService.logoutUser(contact);
        return ResponseEntity.ok("Logout successfully");
    }
}
