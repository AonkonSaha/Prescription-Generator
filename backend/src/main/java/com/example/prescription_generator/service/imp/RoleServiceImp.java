package com.example.prescription_generator.service.imp;

import com.example.prescription_generator.exceptions.RoleNotFoundException;
import com.example.prescription_generator.exceptions.UserNotFoundException;
import com.example.prescription_generator.model.entity.MUser;
import com.example.prescription_generator.model.entity.Role;
import com.example.prescription_generator.repository.RoleRepo;
import com.example.prescription_generator.repository.UserRepo;
import com.example.prescription_generator.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImp implements RoleService {
    private final RoleRepo roleRepo;
    private final UserRepo userRepo;
    @Override
    public Role saveRole(Role role) {
        return roleRepo.save(role);
    }

    @Override
    public Set<Role> findRoleByContact(String contact) {
        return Set.of();
    }

    @Override
    public void deleteSpecificRole(String contact, String roleName) {
        Optional<MUser> mUser=userRepo.findByContact(contact);
        if (mUser.isEmpty()){
            throw new UserNotFoundException("User not found...");
        }
        Set<Role> roles=mUser.get().getRoles();
        boolean found=false;
        for (Role role : roles){
            if (role.getRoleName().equals(roleName)){
                roles.remove(role);
                roleRepo.delete(role);
                found=true;
                break;
            }
        }
        if (!found){
            throw new RoleNotFoundException("Role not found...");
        }
     return;
    }

    @Override
    public Role updateSpecificRole(String contact,String existingRoleName, String newRoleName) {
        Optional<MUser> mUser=userRepo.findByContact(contact);
        if (mUser.isEmpty()){
            throw new UserNotFoundException("User not found...");
        }
        Set<Role> roles=mUser.get().getRoles();
        boolean found=false;
        for (Role role : roles){
            if (role.getRoleName().equals(existingRoleName)){
                role.setRoleName(newRoleName);
                roleRepo.save(role);
                found=true;
                return role;
            }
        }
        if (!found){
            throw new RoleNotFoundException("Role not found...");
        }
        return null;
    }

    @Override
    public Set<Role> findAllRoles() {
        return Set.of();
    }
}
