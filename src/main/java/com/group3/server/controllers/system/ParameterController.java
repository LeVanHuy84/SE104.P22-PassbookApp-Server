package com.group3.server.controllers.system;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group3.server.dtos.system.ParameterRequest;
import com.group3.server.dtos.system.ParameterResponse;
import com.group3.server.services.system.ParameterService;

import lombok.RequiredArgsConstructor;

// Endpoint dành riêng cho admin
@RestController
@RequestMapping("/api/v1/parameters")
@RequiredArgsConstructor
public class ParameterController {
    private final ParameterService parameterService;

    // Endpoint dành riêng cho admin
    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_PARAMETERS')")
    public ParameterResponse getAllParameter() {
        return parameterService.getParameter();
    }

    // Endpoint dành riêng cho admin
    @PostMapping
    @PreAuthorize("hasAuthority('UPDATE_PARAMETER')")
    public ParameterResponse updateParameter(@RequestBody ParameterRequest parameterRequest) {
        return parameterService.updateParameter(parameterRequest);
    }
}
