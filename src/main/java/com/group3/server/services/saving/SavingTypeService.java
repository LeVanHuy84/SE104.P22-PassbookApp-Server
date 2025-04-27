package com.group3.server.services.saving;

import java.util.List;

import org.springframework.stereotype.Service;

import com.group3.server.dtos.saving.SavingTypeRequest;
import com.group3.server.dtos.saving.SavingTypeResponse;
import com.group3.server.mappers.saving.SavingTypeMapper;
import com.group3.server.models.saving.SavingType;
import com.group3.server.repositories.saving.SavingTypeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class SavingTypeService {
    private final SavingTypeRepository savingTypeRepository;
    private final SavingTypeMapper savingTypeMapper;

    public List<SavingTypeResponse> getActiveSavingTypes() {
        try {
            return savingTypeMapper.toDTOs(savingTypeRepository.findByIsActiveTrue());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching saving types", e);
        }
    }

    public List<SavingTypeResponse> getInActiveSavingTypes() {
        try {
            return savingTypeMapper.toDTOs(savingTypeRepository.findByIsActiveFalse());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching saving types", e);
        }
    }

    @Transactional
    public SavingTypeResponse createSavingType(SavingTypeRequest request) {
        try {
            SavingType newType = savingTypeMapper.toEntity(request);
            newType.setActive(true);
            return savingTypeMapper.toDTO(savingTypeRepository.save(newType));
        } catch (RuntimeException e) {
            throw new RuntimeException("Error creating saving type" + e.getMessage());
        }
    }


    @Transactional
    public SavingTypeResponse updateSavingType(Long id, SavingTypeRequest request) {
        try {
            SavingType existing = savingTypeRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Saving type not found"));
            savingTypeMapper.updateEntityFromDto(request, existing);
            return savingTypeMapper.toDTO(savingTypeRepository.save(existing));
        } catch (RuntimeException e) {
            throw new RuntimeException("Error updating saving type" + e.getMessage());
        }
    }

    @Transactional
    public void setSavingTypeActive(Long id, boolean isActive) {
        try {
            SavingType savingType = savingTypeRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Saving type not found"));
            savingType.setActive(isActive);

            // Nếu muốn ngắt liên kết các saving ticket khi disable type, xử lý ở đây
            savingTypeRepository.save(savingType);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error updating saving type status", e);
        }
    }

    @Transactional
    public void deleteSavingType(Long id) {
        try {
            SavingType savingType = savingTypeRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Saving type not found"));

            if (savingType.getSavingTickets() != null && !savingType.getSavingTickets().isEmpty()) {
                throw new RuntimeException("Saving type cannot be deleted because it has saving tickets associated with it.");
            }

            savingTypeRepository.delete(savingType);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error deleting saving type", e);
        }
    }
}

