package com.group3.server.services.auth;

import com.group3.server.utils.TokenType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisTokenService {
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.access-expiration}")
    private Long jwtExpiration;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;


    private static final String ACCESS_TOKEN_KEY = "access-token:";
    private static final String REFRESH_TOKEN_KEY = "refresh-token:";
    private static final String BLACKLIST_KEY = "blacklist:";



    public String getToken(String username, TokenType type) {
        if (type.equals(TokenType.ACCESS)) {
            return redisTemplate.opsForValue().get(ACCESS_TOKEN_KEY + username);
        }
        return redisTemplate.opsForValue().get(REFRESH_TOKEN_KEY + username);
    }

    public void storeAccessToken(String username, String accessToken) {
        String oldAccessToken = getToken(username, TokenType.ACCESS);
        if (oldAccessToken != null) {
            blacklistToken(oldAccessToken, TokenType.ACCESS);
        }
        redisTemplate.opsForValue().set(ACCESS_TOKEN_KEY + username, accessToken, jwtExpiration, TimeUnit.MILLISECONDS);
    }

    public void storeRefreshToken(String username, String refreshToken) {
        String oldRefreshToken = getToken(username, TokenType.REFRESH);
        if (oldRefreshToken != null) {
            blacklistToken(oldRefreshToken, TokenType.REFRESH);
        }
        redisTemplate.opsForValue().set(REFRESH_TOKEN_KEY + username, refreshToken, refreshExpiration, TimeUnit.MILLISECONDS);
    }


    public void blacklistToken(String token, TokenType type) {
        if (type.equals(TokenType.ACCESS)) {
            redisTemplate.opsForValue().set(BLACKLIST_KEY + token, "blacklisted", jwtExpiration, TimeUnit.MILLISECONDS);
        } else {
            redisTemplate.opsForValue().set(BLACKLIST_KEY + token, "blacklisted", refreshExpiration, TimeUnit.MILLISECONDS);
        }
    }


    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_KEY + token));
    }

    public void storeOtp(String email, String otp) {
        redisTemplate.opsForValue().set("otp:" + email, otp, 5, TimeUnit.MINUTES);
    }

    public boolean verify(String email, String otp) {
        String key = "otp:" + email;
        String savedOtp = redisTemplate.opsForValue().get(key);
        if (otp.equals(savedOtp)) {
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }

}
