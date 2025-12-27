package com.disha.fintrack.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.disha.fintrack.dto.JwtResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.disha.fintrack.dto.AuthDTO;
import com.disha.fintrack.dto.ProfileDTO;
import com.disha.fintrack.entity.ProfileEntity;
import com.disha.fintrack.mapper.ProfileMapper;
import com.disha.fintrack.repository.ProfileRepository;
import com.disha.fintrack.service.EmailService;
import com.disha.fintrack.service.ProfileService;
import com.disha.fintrack.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Value("${app.activation.url}")
    private String activationUrl;

    @Override
    public ProfileDTO registerProfile(ProfileDTO profileDTO) {

        if (profileRepository.existsByEmail(profileDTO.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        ProfileEntity entity = profileMapper.toEntity(profileDTO);
        entity.setPassword(passwordEncoder.encode(profileDTO.getPassword()));
        // For local/dev testing enable the account immediately so login/token flow
        // works.
        // In production consider keeping activation flow (set to false) and require
        // email activation.
        entity.setIsActive(true);
        entity.setActivationToken(UUID.randomUUID().toString());

        ProfileEntity saved = profileRepository.save(entity);

        String activationLink = activationUrl + "/api/v1.0/activate?token=" + saved.getActivationToken();
        String subject = "Activate your FinTrack account";
        String body = "Welcome to FinTrack! Please activate your account using the following link: " + activationLink;
        emailService.sendEmail(saved.getEmail(), subject, body);
        return profileMapper.toDto(saved);
    }

    @Override
    public ProfileDTO getProfileByEmail(String email) {
        ProfileEntity entity = profileRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found with email: " + email));
        return profileMapper.toDto(entity);
    }

    @Override
    public ProfileDTO getProfileById(Long id) {
        ProfileEntity entity = profileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found with id: " + id));
        return profileMapper.toDto(entity);
    }

    @Override
    public ProfileDTO updateProfile(Long id, ProfileDTO profileDTO) {

        ProfileEntity entity = profileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found with id: " + id));

        entity.setFullName(profileDTO.getFullName());
        entity.setProfileImageUrl(profileDTO.getProfileImageUrl());

        if (profileDTO.getPassword() != null && !profileDTO.getPassword().isBlank()) {
            entity.setPassword(passwordEncoder.encode(profileDTO.getPassword()));
        }

        ProfileEntity updated = profileRepository.save(entity);
        return profileMapper.toDto(updated);
    }

    @Override
    public void deleteProfile(Long id) {
        if (!profileRepository.existsById(id)) {
            throw new IllegalArgumentException("Profile not found with id: " + id);
        }
        profileRepository.deleteById(id);
    }

    @Override
    public List<ProfileDTO> getAllInactiveProfiles() {
        return profileRepository.findByIsActiveFalse()
                .stream()
                .map(profileMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean activateProfile(String activationToken) {
        // Placeholder logic (real version uses token table or JWT)
        ProfileEntity entity = profileRepository.findByActivationToken(activationToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid activation token"));
        entity.setIsActive(true);
        // entity.setActivationToken(null); // Clear token after activation
        profileRepository.save(entity);
        return true;

    }

    @Override
    public boolean isAccountActive(String email) {
        ProfileEntity entity = profileRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found with email: " + email));
        return entity.getIsActive();
    }

    @Override
    public ProfileEntity getCurrentProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return profileRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Profile not found with email: " + authentication.getName()));

    }

    @Override
    public ProfileDTO getPublicProfile(String email) {
        ProfileEntity currentProfile = null;
        if (email == null || email.isBlank()) {
            currentProfile = getCurrentProfile();
        } else {
            currentProfile = profileRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Profile not found with email: " + email));
        }

        return profileMapper.toDto(currentProfile);

    }

    @Override
    public JwtResponse authenticateAndGenerateToken(AuthDTO authDTO) {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(), authDTO.getPassword()));
            // generate JWT token
            String token = jwtUtil.generateToken(authDTO.getEmail());
            return JwtResponse.builder()
                    .token(token)
                    .tokenType("Bearer")
                    .message("Authentication successful")
                    .user(getPublicProfile(authDTO.getEmail()))
                    .build();
        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            throw e;
        } catch (Exception e) {
            throw new org.springframework.security.authentication.BadCredentialsException("Invalid email or password");
        }
    }
}
