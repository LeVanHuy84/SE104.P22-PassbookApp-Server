package com.group3.server.services.auth;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.group3.server.dtos.auth.LoginRequest;
import com.group3.server.dtos.auth.RegisterRequest;
import com.group3.server.dtos.auth.TokenResponse;
import com.group3.server.models.auth.Group;
import com.group3.server.models.auth.User;
import com.group3.server.repositories.auth.GroupRepository;
import com.group3.server.repositories.auth.UserRepository;
import com.group3.server.repositories.system.ParameterRepository;
import com.group3.server.services.JwtService;
import com.group3.server.utils.EmailUtil;
import com.group3.server.utils.TokenType;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthenticationService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager manager;
    private final JwtService jwtService;
    private final RedisTokenService redisTokenService;
    private final MailService mailService;
    private final ParameterRepository parameterRepository;

    private TokenResponse generateAndStoreTokens(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        redisTokenService.storeAccessToken(user.getUsername(), accessToken);
        redisTokenService.storeRefreshToken(user.getUsername(), refreshToken);

        return TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).userId(user.getId())
                .permissions(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()).build();
    }

    public TokenResponse authenticate(LoginRequest request) {
        try {
            manager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Email: " + request.getEmail() + " không tồn tại"));

            return generateAndStoreTokens(user);

        } catch (RuntimeException e) {
            log.error("Error authenticate", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public TokenResponse register(RegisterRequest request) {
        try {
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

            Group group = groupRepository.findByName("CUSTOMER")
                    .orElseThrow(() -> new RuntimeException("Nhóm quyền 'CUSTOMER' không tồn tại"));

            User user = userRepository.save(User.builder().password(encoder.encode(request.getPassword()))
                    .email(request.getEmail()).phone(request.getPhone()).fullName(request.getFullName())
                    .dateOfBirth(request.getDateOfBirth()).citizenID(request.getCitizenID())
                    .address(request.getAddress()).group(group).build());

            return generateAndStoreTokens(user);

        } catch (RuntimeException e) {
            log.error("Register errors", e);
            throw new RuntimeException("Lỗi đăng ký\n" + e.getMessage());
        }
    }

    public TokenResponse refresh(String refreshToken) {

        String username = jwtService.extractUsername(refreshToken, TokenType.REFRESH);
        User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("Email not found"));
        if (!jwtService.isValidToken(refreshToken, user, TokenType.REFRESH)
                || !refreshToken.equals(redisTokenService.getToken(username, TokenType.REFRESH))
                || redisTokenService.isTokenBlacklisted(refreshToken)) {
            log.error("Invalid refresh token");
            throw new RuntimeException("Lỗi token");
        }

        String oldAccessToken = redisTokenService.getToken(user.getUsername(), TokenType.ACCESS);
        redisTokenService.blacklistToken(oldAccessToken, TokenType.ACCESS);

        return TokenResponse.builder().accessToken(jwtService.generateAccessToken(user)).refreshToken(refreshToken)
                .userId(user.getId())
                .permissions(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();

    }

    public void logout(String token) {
        String username = jwtService.extractUsername(token, TokenType.ACCESS);
        if (!token.equals(redisTokenService.getToken(username, TokenType.ACCESS))) {
            log.error("Invalid access token");
            throw new RuntimeException("Lỗi token");
        }

        String accessToken = redisTokenService.getToken(username, TokenType.ACCESS);
        String refreshToken = redisTokenService.getToken(username, TokenType.REFRESH);

        redisTokenService.blacklistToken(accessToken, TokenType.ACCESS);
        redisTokenService.blacklistToken(refreshToken, TokenType.REFRESH);

    }

    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email: " + email + " không tồn tại"));
        String newPassWord = EmailUtil.generateRandomPassword();
        String subject = "Đặt lại mật khẩu";
        String body = "Mật khẩu tài khoản mới của bạn là: " + newPassWord;
        mailService.sendEmail(email, subject, body);
        user.setPassword(encoder.encode(newPassWord));
        userRepository.save(user);
    }

    public void changePassword(String oldPassword, String newPassword) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

        if (oldPassword.equals(newPassword)) {
            throw new RuntimeException("Mật khẩu mới không được trùng với mật khẩu cũ");
        }

        if (!encoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không đúng");
        }

        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
    }

    public void sendOtp(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email: " + email + " đã được đăng ký");
        }
        String subject = "Xác thực tài khoản";
        String otp = EmailUtil.generateOtp();
        String body = "Mã xác thực tài khoản của bạn là " + otp;
        redisTokenService.storeOtp(email, otp);
        mailService.sendEmail(email, subject, body);
    }

    public boolean verify(String email, String otp) {
        return redisTokenService.verify(email, otp);
    }

}
