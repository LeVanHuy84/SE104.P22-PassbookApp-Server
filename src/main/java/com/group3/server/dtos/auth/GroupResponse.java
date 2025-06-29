package com.group3.server.dtos.auth;

import java.util.List;

import com.group3.server.models.auth.Permission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupResponse {
    private Integer id;
    private String name;
    private String description;
    private List<Permission> permissions;
}
