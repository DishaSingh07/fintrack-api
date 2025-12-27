package com.disha.fintrack.service;

import java.util.List;

import com.disha.fintrack.dto.AuthDTO;
import com.disha.fintrack.dto.JwtResponse;
import com.disha.fintrack.dto.ProfileDTO;
import com.disha.fintrack.entity.ProfileEntity;

public interface ProfileService {

    ProfileDTO registerProfile(ProfileDTO profileDTO);

    ProfileDTO getProfileByEmail(String email);

    void deleteProfile(Long id);

    List<ProfileDTO> getAllInactiveProfiles();

    ProfileDTO getProfileById(Long id);

    ProfileDTO updateProfile(Long id, ProfileDTO profileDTO);

    boolean activateProfile(String activationToken);

    boolean isAccountActive(String email);

    ProfileEntity getCurrentProfile();

    ProfileDTO getPublicProfile(String email);

    JwtResponse authenticateAndGenerateToken(AuthDTO authDTO);


}