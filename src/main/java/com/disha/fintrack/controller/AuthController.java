package com.disha.fintrack.controller;

import com.disha.fintrack.dto.JwtResponse;
import com.disha.fintrack.dto.ProfileDTO;
import com.disha.fintrack.entity.ProfileEntity;
import com.disha.fintrack.mapper.ProfileMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.disha.fintrack.common.ApiResponse;
import com.disha.fintrack.dto.AuthDTO;
import com.disha.fintrack.service.impl.ProfileServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final ProfileServiceImpl profileService;
    private final ProfileMapper profileMapper;


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@RequestBody AuthDTO authDTO) {
        JwtResponse jwt = profileService.authenticateAndGenerateToken(authDTO);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + jwt.getToken())
                .body(ApiResponse.success(jwt, "Login successful"));
    }


//    logout controller
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        // Invalidate the JWT token on the client side by simply removing it.
        // Optionally, you can implement server-side token blacklisting if needed.
        return ResponseEntity.ok(ApiResponse.success("Logout successful"));
    }

    @PostMapping("/me")
    public ResponseEntity<ApiResponse<ProfileDTO>> getCurrentProfile() {
        ProfileEntity profile = profileService.getCurrentProfile();
        return ResponseEntity.ok(ApiResponse.success(profileMapper.toDto(profile), "Profile fetched successfully"));
    }



}
