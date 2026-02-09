package com.disha.fintrack.controller;

import java.util.Map;

import com.disha.fintrack.common.ApiResponse;
import com.disha.fintrack.dto.ProfileDTO;
import com.disha.fintrack.entity.ProfileEntity;
import com.disha.fintrack.mapper.ProfileMapper;
import com.disha.fintrack.service.impl.ProfileServiceImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.disha.fintrack.service.DashboardService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profiles")
@CrossOrigin("*")
public class ProfileController {

    private final ProfileServiceImpl profileService;
    private final DashboardService dashboardService;
    private final ProfileMapper profileMapper;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<ProfileDTO>> registerProfile(@RequestBody ProfileDTO profileDTO) {
        ProfileDTO createdProfile = profileService.registerProfile(profileDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdProfile, "Profile registered successfully"));
    }

    @GetMapping("/activate")
    public ResponseEntity<ApiResponse<String>> activateProfile(@RequestParam String token) {
        profileService.activateProfile(token);
        return ResponseEntity.ok(ApiResponse.success("Profile activated successfully"));
    }

    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> test() {
        return ResponseEntity.ok(ApiResponse.success("Test Successful"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ProfileDTO>> getProfile(@RequestParam Long id) {
        ProfileDTO profile = profileService.getProfileById(id);
        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ProfileDTO>> updateProfile(@RequestParam Long id,
            @RequestBody ProfileDTO profileDTO) {
        ProfileDTO updatedProfile = profileService.updateProfile(id, profileDTO);
        return ResponseEntity.ok(ApiResponse.success(updatedProfile, "Profile updated successfully"));

    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getProfileSummary() {
        Map<String, Object> summary = dashboardService.getProfileSummary();
        return ResponseEntity.ok(ApiResponse.success(summary, "Profile summary fetched"));

    }

    @GetMapping("/current")
    public ResponseEntity<ApiResponse<ProfileDTO>> getCurrentProfile() {
        ProfileEntity profile = profileService.getCurrentProfile();
        return ResponseEntity.ok(ApiResponse.success(profileMapper.toDto(profile), "Profile fetched successfully"));

    }


}
