package com.group3.server.repositories.system;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group3.server.models.system.Parameter;

public interface ParameterRepository extends JpaRepository<Parameter, Long> {

}
