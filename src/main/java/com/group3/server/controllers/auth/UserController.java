package com.group3.server.controllers.auth;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group3.server.dtos.Filter.UserFilter;
import com.group3.server.dtos.auth.UserResponse;
import com.group3.server.dtos.responses.PageResponse;
import com.group3.server.services.auth.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //Endpoint dành cho admin/staff
    @GetMapping
    public ResponseEntity<PageResponse<UserResponse>> getUsers(
            @ModelAttribute UserFilter filter,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            @RequestParam(defaultValue = "asc", required = false) String order) {

        Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<UserResponse> users = userService.getUsers(filter, pageable);

        PageResponse<UserResponse> response = PageResponse.<UserResponse>builder()
                .content(users.getContent())
                .page(users.getNumber())
                .size(users.getSize())
                .totalElements(users.getTotalElements())
                .totalPages(users.getTotalPages())
                .last(users.isLast())
                .first(users.isFirst())
                .build();

        return ResponseEntity.ok(response);
    }
}
