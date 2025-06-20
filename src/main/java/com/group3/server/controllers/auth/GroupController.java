package com.group3.server.controllers.auth;

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

import com.group3.server.dtos.auth.GroupRequest;
import com.group3.server.dtos.auth.GroupResponse;
import com.group3.server.models.auth.Permission;
import com.group3.server.services.auth.GroupService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @GetMapping("/permissions")
    public ResponseEntity<List<Permission>> getAllPermissions() {
        List<Permission> permissions = groupService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }

    // Endpoint dành cho admin/staff
    @GetMapping("/groups")
    @PreAuthorize("hasAuthority('VIEW_PARAMETERS')")
    public ResponseEntity<List<GroupResponse>> getAllGroups() {
        List<GroupResponse> groups = groupService.getAllGroupsNotAdmin();
        return ResponseEntity.ok(groups);
    }

    @PostMapping("/groups")
    public ResponseEntity<GroupResponse> createGroup(@RequestBody GroupRequest request) {
        GroupResponse groupResponse = groupService.createGroup(request);
        return ResponseEntity.ok(groupResponse);
    }

    @PutMapping("/groups/{id}")
    public ResponseEntity<GroupResponse> updateGroup(@PathVariable Integer id, @RequestBody GroupRequest request) {
        GroupResponse groupResponse = groupService.updateGroup(id, request);
        return ResponseEntity.ok(groupResponse);
    }

    @DeleteMapping("/groups/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Integer id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

}
