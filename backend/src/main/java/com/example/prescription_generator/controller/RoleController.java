package com.example.prescription_generator.controller;

import com.example.prescription_generator.model.dto.RoleDTO;
import com.example.prescription_generator.model.mapper.RoleMapper;
import com.example.prescription_generator.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Role Management API", description = "Endpoints for assigning, updating, and deleting user roles")
public class RoleController {

    private final RoleService roleService;
    private final RoleMapper roleMapper;

    @Operation(
            summary = "Assign a new role to a user",
            description = "Assigns a new role (e.g., ADMIN, DOCTOR) to a user by mobile number"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role assigned successfully",
                    content = @Content(schema = @Schema(implementation = RoleDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/v1/assign")
    public ResponseEntity<RoleDTO> registerRole(@RequestBody RoleDTO roleDTO) {
        return ResponseEntity.ok(roleMapper.toRoleDTO(
                roleService.saveRole(roleMapper.toRole(roleDTO))));
    }

    @Operation(
            summary = "Update an existing role for a user",
            description = "Updates an existing role for a user identified by mobile number"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role updated successfully",
                    content = @Content(schema = @Schema(implementation = RoleDTO.class))),
            @ApiResponse(responseCode = "404", description = "User or role not found",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/v1/update")
    public ResponseEntity<RoleDTO> updateRoleV1(@RequestBody RoleDTO roleDTO) {
        return ResponseEntity.ok(roleMapper.toRoleDTO(roleService.updateSpecificRole(
                roleDTO.getMobileNumber(),
                roleDTO.getExistingRoleName(),
                roleDTO.getNewRoleName())));
    }

    @Operation(
            summary = "Delete a role from a user",
            description = "Deletes a specific role from a user based on their mobile number"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role deleted successfully",
                    content = @Content(schema = @Schema(example = "Deleted successfully!"))),
            @ApiResponse(responseCode = "404", description = "Role not found",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("/v1/delete")
    public ResponseEntity<String> deleteRoleV1(@RequestBody RoleDTO roleDTO) {

        roleService.deleteSpecificRole(roleDTO.getMobileNumber(), roleDTO.getDeletedRoleName());
        return ResponseEntity.ok("Deleted successfully!");
    }
}
