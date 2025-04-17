package com.group3.server.services.auth;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.group3.server.dtos.auth.LoginRequest;
import com.group3.server.dtos.auth.RegisterRequest;
import com.group3.server.dtos.auth.TokenResponse;
import com.group3.server.models.auth.User;
import com.group3.server.repositories.auth.UserRepository;
import com.group3.server.services.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager manager;
    private final JwtService jwtService;

    public TokenResponse authenticate(LoginRequest request) {
        try {
            manager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("Username: " + request.getUsername() + " not found"));
            return TokenResponse.builder()
                    .accessToken(jwtService.generateToken(user))
                    .refreshToken("refresh_token")
                    .userId(user.getId())
                    .build();
        } catch (Exception e) {
            log.error("Error authenticate", e);
            throw new RuntimeException("Error authenticate" + e.getMessage());
        }
    }

    public TokenResponse register(RegisterRequest request) {
        try {
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new RuntimeException("Username: " + request.getUsername() + " has exist");
            }
            User newUser = User.builder()
                    .username(request.getUsername())
                    .password(encoder.encode(request.getPassword()))
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .fullname(request.getFullName())
                    .dateOfBirth(request.getDateOfBirth())
                    .citizenID(request.getCitizenID())
                    .address(request.getAddress())
                    .balance(request.getBalance())
                    .build();

            return TokenResponse.builder()
                    .accessToken(jwtService.generateToken(newUser))
                    .refreshToken("refresh_token")
                    .userId(userRepository.save(newUser).getId())
                    .build();
        } catch (Exception e) {
            log.error("Register errors", e);
            throw new RuntimeException("Register errors" + e.getMessage());
        }
    }
}
