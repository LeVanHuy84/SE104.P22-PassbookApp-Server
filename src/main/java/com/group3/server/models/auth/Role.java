package com.group3.server.models.auth;

import java.util.HashSet;
import java.util.Set;

import com.group3.server.models.BaseModel;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role extends BaseModel<Integer> {
    private String name;
    private String description;

    @OneToMany(mappedBy = "role")
    private Set<RolePermission> roleHasPermisstions = new HashSet<>();
}
