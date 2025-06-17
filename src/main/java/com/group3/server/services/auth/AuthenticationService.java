package com.group3.server.services.auth;


import java.time.LocalDate;
import java.time.Period;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.group3.server.dtos.auth.LoginRequest;
import com.group3.server.dtos.auth.RegisterRequest;
import com.group3.server.dtos.auth.TokenResponse;
import com.group3.server.models.auth.User;
import com.group3.server.repositories.auth.UserRepository;
import com.group3.server.repositories.system.ParameterRepository;
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
    private final ParameterRepository parameterRepository;

    public TokenResponse authenticate(LoginRequest request) {
        try {
            manager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("Tài khoản: " + request.getUsername() + " không tồn tại"));
            return TokenResponse.builder()
                    .accessToken(jwtService.generateToken(user))
                    .refreshToken("refresh_token")
                    .userId(user.getId())
                    .build();
        } catch (RuntimeException e) {
            log.error("Lỗi xác thực", e);
            throw new RuntimeException("Lỗi xác thực " + e.getMessage());
        }
    }

    public TokenResponse register(RegisterRequest request) {
        try {
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new RuntimeException("Tài khoản: " + request.getUsername() + " đã tồn tại");
            }

            // Lấy tuổi tối thiểu từ ParameterRepository
            int minAge = parameterRepository.findById(1L)
                                            .orElseThrow(() -> new RuntimeException("Lỗi tham số hệ thống"))
                                            .getMinAge();

            // Tính tuổi của người dùng từ ngày sinh
            LocalDate birthDate = request.getDateOfBirth();
            int userAge = Period.between(birthDate, LocalDate.now()).getYears();

            if (userAge < minAge) {
                throw new RuntimeException("Người dùng không được dưới " + minAge + " tuổi");
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
                    .build();

            return TokenResponse.builder()
                    .accessToken(jwtService.generateToken(newUser))
                    .refreshToken("refresh_token")
                    .userId(userRepository.save(newUser).getId())
                    .build();
        } catch (RuntimeException e) {
            log.error("Register errors", e);
            throw new RuntimeException("Lỗi đăng ký: " + e.getMessage());
        }
    }
}
