package com.example.prescription_generator.controller;

import com.example.prescription_generator.exceptions.InvalidUserArgumentException;
import com.example.prescription_generator.exceptions.UserNotFoundException;
import com.example.prescription_generator.model.dto.UserDTO;
import com.example.prescription_generator.model.mapper.UserMapper;
import com.example.prescription_generator.service.UserService;
import com.example.prescription_generator.service.UserValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin API", description = "Admin operations for managing users")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final UserValidationService userValidationService;
    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(
            summary = "Delete a user by contact number",
            description = "Deletes a user if the provided mobile contact exists and is valid"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully",
                    content = @Content(schema = @Schema(example = "Successfully deleted user!"))),
            @ApiResponse(responseCode = "400", description = "Invalid input (e.g., contact is blank)",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("/v1/user/delete")
    public ResponseEntity<String> deleteUser(
            @Parameter(description = "Contact number of the user to delete", example = "01881264859")
            @RequestParam String contact) {

        if (userValidationService.isEmptyUserContact(contact)) {
            throw new InvalidUserArgumentException("Contact can't be empty");
        }
        if (!userValidationService.isExitUserByContact(contact)) {
            throw new UserNotFoundException("User not found");
        }
        userService.deleteUser(contact);
        return ResponseEntity.ok("Successfully deleted user!");
    }

    @Operation(
            summary = "Get all registered users",
            description = "Returns a list of all registered users in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully",
                    content = @Content(schema = @Schema(implementation = UserDTO.class)))
    })
    @GetMapping("/v1/user/get-all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userMapper.toUserDTOS(userService.findAllUsers()));
    }
}
