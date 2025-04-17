package com.group3.server.models.auth;

import java.util.HashSet;
import java.util.Set;

import com.group3.server.models.BaseModel;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "groups")
public class Group extends BaseModel<Integer> {
    private String name;
    private String description;

    @OneToOne
    private Role role;

    @OneToMany(mappedBy = "group")
    private Set<GroupUser> groupUsers = new HashSet<>();

}
