package com.novabank.controller;

import com.novabank.dto.LoginRequestDTO;
import com.novabank.dto.LoginResponseDTO;
import com.novabank.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(),
                        dto.getPassword()
                )
        );

        String token = jwtService.generarToken(dto.getUsername());

        LoginResponseDTO response = new LoginResponseDTO(
                token,
                "Bearer",
                System.currentTimeMillis() + 86400000 // 24h
        );

        return ResponseEntity.ok(response);
    }
}