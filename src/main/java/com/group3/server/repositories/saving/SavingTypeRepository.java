package com.group3.server.repositories.saving;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group3.server.models.saving.SavingType;

public interface SavingTypeRepository extends JpaRepository<SavingType, Long>{
    List<SavingType> findByIsActiveFalse();
    List<SavingType> findByIsActiveTrue();

    SavingType findByDuration(int duration);
}
