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
            throw new RuntimeException("Lỗi truy cập danh sách loại tiết kiệm", e);
        }
    }

    public List<SavingTypeResponse> getInActiveSavingTypes() {
        try {
            return savingTypeMapper.toDTOs(savingTypeRepository.findByIsActiveFalse());
        } catch (RuntimeException e) {
            throw new RuntimeException("Lỗi truy cập danh sách loại tiết kiệm đã vô hiệu", e);
        }
    }

    @Transactional
    public SavingTypeResponse createSavingType(SavingTypeRequest request) {
        try {
            savingTypeRepository.findByTypeName(request.getTypeName())
                    .ifPresent(type -> {
                        throw new RuntimeException("Loại tiết kiệm với tên " + request.getTypeName() + " đã tồn tại");
                    });
            
            if (savingTypeRepository.findByDuration(request.getDuration()) != null) {
                throw new RuntimeException("Loại tiết kiệm với thời hạn " + request.getDuration() + " đã tồn tại");
            }
            
            SavingType newType = savingTypeMapper.toEntity(request);
            return savingTypeMapper.toDTO(savingTypeRepository.save(newType));
        } catch (RuntimeException e) {
            throw new RuntimeException("Lỗi tạo mới loại tiết kiệm" + e.getMessage());
        }
    }

    @Transactional
    public SavingTypeResponse updateSavingType(Long id, SavingTypeRequest request) {
        try {
            SavingType existing = savingTypeRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Loại tiết kiệm không tồn tại"));
            
            if (existing.getDuration() == 0) {
                // Không cho sửa duration và name
                if (!request.getTypeName().equals(existing.getTypeName())) {
                    throw new RuntimeException("Không thể sửa tên của loại tiết kiệm không kỳ hạn.");
                }
                if (request.getDuration() != 0) {
                    throw new RuntimeException("Không thể thay đổi thời hạn của loại tiết kiệm không kỳ hạn.");
                }
            }
            
            savingTypeMapper.updateEntityFromDto(request, existing);
            return savingTypeMapper.toDTO(savingTypeRepository.save(existing));
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional
    public void setSavingTypeActive(Long id, boolean isActive) {
        try {
            SavingType savingType = savingTypeRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Loại tiết kiệm không tồn tại"));
            if (savingType.getDuration() == 0) {
                throw new RuntimeException("Không thể ẩn tiết kiệm không kỳ hạn");
            }
            savingType.setActive(isActive);

            // Nếu muốn ngắt liên kết các saving ticket khi disable type, xử lý ở đây
            savingTypeRepository.save(savingType);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Transactional
    public void deleteSavingType(Long id) {
        try {
            SavingType savingType = savingTypeRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Loại tiết kiệm không tồn tại"));

            if (savingType.getSavingTickets() != null && !savingType.getSavingTickets().isEmpty()) {
                throw new RuntimeException("Không thể xóa loại tiết kiệm này vì nó đang được sử dụng trong các phiếu gửi tiết kiệm");
            }
            if (savingType.getDuration() == 0) {
                throw new RuntimeException("Không thể xóa loại tiết kiệm không kỳ hạn");
            }

            savingTypeRepository.delete(savingType);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}

