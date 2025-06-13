package com.group3.server.services.auth;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.group3.server.dtos.Filter.UserFilter;
import com.group3.server.dtos.Specification.UserSpecification;
import com.group3.server.dtos.auth.UserResponse;
import com.group3.server.mappers.auth.UserMapper;
import com.group3.server.models.auth.User;
import com.group3.server.repositories.auth.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<UserResponse> findUsers(UserFilter filter) {
        Specification<User> specification = UserSpecification.withFilter(filter);
        return userRepository.findAll(specification)
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

        public Page<UserResponse> getUsers(UserFilter filter, Pageable pageable) {
        try {
            Specification<User> specification = UserSpecification.withFilter(filter);
            Page<User> users = userRepository.findAll(specification, pageable);
            return users.map(userMapper::toDTO);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching saving tickets", e);
        }
    }
}
