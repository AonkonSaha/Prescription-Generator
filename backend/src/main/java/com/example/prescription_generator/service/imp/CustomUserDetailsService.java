package com.example.prescription_generator.service.imp;

import com.example.prescription_generator.exceptions.UserContactNotFoundException;
import com.example.prescription_generator.exceptions.UserNotFoundException;
import com.example.prescription_generator.model.entity.MUser;
import com.example.prescription_generator.model.entity.Role;
import com.example.prescription_generator.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepo userRepo;

    @Override
        public UserDetails loadUserByUsername(String contact) {
            Optional<MUser> user = userRepo.findByContact(contact);
            if(user.isEmpty()) {
                throw new UserContactNotFoundException("User not found");
            }
            Set<SimpleGrantedAuthority> authorities = new HashSet<>();
            for (Role role : user.get().getRoles() ){
                authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName()));
            }
            return new org.springframework.security.core.userdetails.User(
                    user.get().getContact(),
                    user.get().getPassword(),
                    authorities
            );
        }
}
