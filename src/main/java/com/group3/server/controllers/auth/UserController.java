package com.group3.server.controllers.auth;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group3.server.dtos.Filter.UserFilter;
import com.group3.server.dtos.auth.UserResponse;
import com.group3.server.services.auth.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //Endpoint dành cho staff
    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers(@ModelAttribute UserFilter filter) {
        return ResponseEntity.ok(userService.getUsers(filter));
    }
}
