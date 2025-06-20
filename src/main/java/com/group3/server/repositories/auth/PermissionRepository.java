package com.group3.server.repositories.auth;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group3.server.models.auth.Permission;

@Repository
public interface PermissionRepository  extends JpaRepository<Permission, Integer>{
    
}
