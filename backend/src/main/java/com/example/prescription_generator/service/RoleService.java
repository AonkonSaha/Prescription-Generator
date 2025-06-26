package com.example.prescription_generator.service;

import com.example.prescription_generator.model.entity.Role;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface RoleService {
    Role saveRole(Role role);
    Set<Role> findRoleByContact(String contact);
    void deleteSpecificRole(String contact, String deletedRoleName);
    Role updateSpecificRole(String contact,String existingRoleName,String newRoleName);
    Set<Role> findAllRoles();
}
