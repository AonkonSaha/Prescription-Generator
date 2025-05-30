package com.example.prescription_generator.service.imp;

import com.example.prescription_generator.model.entity.MUser;
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
        public UserDetails loadUserByUsername(String contact) throws UsernameNotFoundException {
            Optional<MUser> user = userRepo.findByContact(contact);
            if(user.isEmpty()) {
                throw new UsernameNotFoundException("User not found...");
            }
            Set<SimpleGrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_DOCTOR"));
            return new org.springframework.security.core.userdetails.User(
                    user.get().getContact(),
                    user.get().getPassword(),
                    authorities
            );
        }
}
