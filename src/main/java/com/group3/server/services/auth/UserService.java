package com.group3.server.services.auth;

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
import com.group3.server.utils.AuthUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Tài khoảng không tồn tại"));
    }

    public UserResponse getMyInfo() {
        Long myId = AuthUtils.getCurrentUserId();
        User user = userRepository.findById(myId)
                .orElseThrow(() -> new RuntimeException("Tài khoảng không tồn tại"));
        return userMapper.toDTO(user);
    }

    public Page<UserResponse> getUsers(UserFilter filter, Pageable pageable) {
        try {
            Specification<User> specification = UserSpecification.withFilter(filter);
            Page<User> users = userRepository.findAll(specification, pageable);
            return users.map(userMapper::toDTO);
        } catch (RuntimeException e) {
            throw new RuntimeException("Lỗi truy cập danh sách tài khoản", e);
        }
    }

    public void setUserActive(Long userId, boolean active) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));
        user.setActive(active);
        userRepository.save(user);
    }
}
