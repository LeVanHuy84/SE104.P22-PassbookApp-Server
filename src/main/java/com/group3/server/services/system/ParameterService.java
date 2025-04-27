package com.group3.server.services.system;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.group3.server.dtos.system.ParameterRequest;
import com.group3.server.dtos.system.ParameterResponse;
import com.group3.server.mappers.system.ParameterMapper;
import com.group3.server.models.system.Parameter;
import com.group3.server.repositories.system.ParameterRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ParameterService {
    private final ParameterRepository parameterRepository;
    private final ParameterMapper parameterMapper;

    public ParameterResponse getParameter(){
        try {
            return parameterMapper.toDTO(parameterRepository.findById(1L).orElseThrow());
        } catch(RuntimeException e) {
            throw new RuntimeException("Error fetching parameter" + e.getMessage());
        }
    }

    public void createParameter(int minAge, BigDecimal minTransactionAmount, BigDecimal maxTransactionAmount, BigDecimal minSavingAmount) {
        try {
            Parameter entity = new Parameter();
            entity.setMinAge(minAge);
            entity.setMinTransactionAmount(minTransactionAmount);
            entity.setMaxTransactionAmount(maxTransactionAmount);
            entity.setMinSavingAmount(minSavingAmount);

            parameterRepository.saveAndFlush(entity);
        } catch(RuntimeException e) {
            throw new RuntimeException("Error fetching parameter" + e.getMessage());
        }
    }

    public ParameterResponse updateParameter(ParameterRequest request) {
        try {
            Parameter entity = parameterRepository.findById(1L).orElseThrow();
            parameterMapper.updateEntityFromDto(request, entity);
            return parameterMapper.toDTO(parameterRepository.saveAndFlush(entity));
        } catch(RuntimeException e) {
            throw new RuntimeException("Error fetching parameter" + e.getMessage());
        }
    }
}
