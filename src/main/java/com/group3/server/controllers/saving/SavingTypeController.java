package com.group3.server.controllers.saving;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group3.server.dtos.saving.SavingTypeRequest;
import com.group3.server.dtos.saving.SavingTypeResponse;
import com.group3.server.services.saving.SavingTypeService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/saving-types")
@RequiredArgsConstructor
public class SavingTypeController {
    private final SavingTypeService savingTypeService;

    //Endpoint dành cho admin/staff/customer
    @GetMapping("/active")
    @PreAuthorize("hasAnyAuthority('VIEW_ACTIVE_SAVINGTYPES')")
    public ResponseEntity<List<SavingTypeResponse>> getActiveSavingTypes() {
        return ResponseEntity.ok(savingTypeService.getActiveSavingTypes());
    }

    //Endpoint dành cho admin
    @GetMapping("/inactive")
    @PreAuthorize("hasAuthority('VIEW_INACTIVE_SAVINGTYPES')")
    public ResponseEntity<List<SavingTypeResponse>> getInactiveSavingTypes() {
        return ResponseEntity.ok(savingTypeService.getInActiveSavingTypes());
    }

    //Endpoint dành cho admin
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_SAVINGTYPE')")
    public ResponseEntity<SavingTypeResponse> createSavingType(@RequestBody SavingTypeRequest request) {
        return ResponseEntity.ok(savingTypeService.createSavingType(request));
    }


    // Endpoint dành cho admin
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_SAVINGTYPE')")
    public ResponseEntity<SavingTypeResponse> updateSavingType(@PathVariable Long id, @RequestBody SavingTypeRequest request) {
        return ResponseEntity.ok(savingTypeService.updateSavingType(id, request));
    }

    // Endpoint dành cho admin
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_SAVINGTYPE')")
    public ResponseEntity<Void> deleteSavingType(@PathVariable Long id) {
        savingTypeService.deleteSavingType(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint dành cho admin
    @PutMapping("/set-active/{id}")
    @PreAuthorize("hasAuthority('SET_ACTIVE_SAVINGTYPE')")
    public ResponseEntity<Void> setSavingTypeActive(@PathVariable Long id, @RequestBody boolean isActive) {
        savingTypeService.setSavingTypeActive(id, isActive);
        return ResponseEntity.noContent().build();
    }
}
