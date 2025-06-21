package com.group3.server.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.group3.server.models.auth.Group;
import com.group3.server.models.auth.User;
import com.group3.server.repositories.auth.GroupRepository;
import com.group3.server.repositories.auth.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountConfig implements CommandLineRunner {

    @Value("${ADMIN_ACCOUNT_EMAIL}")
    private String adminEmail;

    @Value("${ADMIN_ACCOUNT_PASSWORD}")
    private String adminPassword;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GroupRepository groupRepository;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail(adminEmail).isPresent()) {
            return;
        }

        Group group = groupRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("Default group 'ADMIN' not found"));

        userRepository.save(User.builder().email(adminEmail).password(passwordEncoder.encode(adminPassword))
                .fullName("Administrator").phone(null).dateOfBirth(null).citizenID("000000000000").address(null)
                .group(group).build());
    }
}
