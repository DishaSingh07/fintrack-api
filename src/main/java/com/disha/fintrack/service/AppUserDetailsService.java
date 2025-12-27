package com.disha.fintrack.service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.disha.fintrack.entity.ProfileEntity;
import com.disha.fintrack.repository.ProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Implementation to load user details by username (email)
        ProfileEntity existingUserProfile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (existingUserProfile.getIsActive() == null || !existingUserProfile.getIsActive()) {
            throw new org.springframework.security.authentication.DisabledException("Account is not active");
        }

        return User.builder()
                .username(existingUserProfile.getEmail())
                .password(existingUserProfile.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
                .build();

    }
}
