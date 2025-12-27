package com.disha.fintrack.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.disha.fintrack.dto.AuthDTO;
import com.disha.fintrack.service.impl.ProfileServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ProfileServiceImpl profileService;

    @PostMapping("/login")
    public ResponseEntity<com.disha.fintrack.dto.JwtResponse> login(@RequestBody AuthDTO authDTO) {
        com.disha.fintrack.dto.JwtResponse jwt = profileService.authenticateAndGenerateToken(authDTO);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + jwt.getToken())
                .body(jwt);
    }


}
