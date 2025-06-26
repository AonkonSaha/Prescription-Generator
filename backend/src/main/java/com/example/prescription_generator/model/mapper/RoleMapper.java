package com.example.prescription_generator.model.mapper;

import com.example.prescription_generator.exceptions.UserNotFoundException;
import com.example.prescription_generator.model.dto.RoleDTO;
import com.example.prescription_generator.model.entity.MUser;
import com.example.prescription_generator.model.entity.Role;
import com.example.prescription_generator.repository.UserRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoleMapper {
    private final UserRepo userRepo;
    public Role toRole(RoleDTO roleDTO) {
        Role role= Role.builder()
                .roleName(roleDTO.getNewRoleName())
                .roleDescription(roleDTO.getDescription())
                .build();
        Optional<MUser> mUser=userRepo.findByContact(roleDTO.getMobileNumber());
        if (mUser.isEmpty()){
            throw new UserNotFoundException("User not found...");
        }
        mUser.get().getRoles().add(role);
        role.getUsers().add(mUser.get());
        return role;
    }

    public RoleDTO toRoleDTO(Role role) {
        return RoleDTO.builder()
                .newRoleName(role.getRoleName())
                .description(role.getRoleDescription())
                .mobileNumber(role.getRoleDescription())
                .build();
    }
}
