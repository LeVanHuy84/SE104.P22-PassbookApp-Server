package com.group3.server.repositories.auth;

import com.group3.server.models.auth.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

    Optional<Group> findByName(String name);
}
