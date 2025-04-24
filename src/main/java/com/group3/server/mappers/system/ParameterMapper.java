package com.group3.server.mappers.system;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.group3.server.dtos.system.ParameterRequest;
import com.group3.server.dtos.system.ParameterResponse;
import com.group3.server.models.system.Parameter;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ParameterMapper {
    Parameter toEntity(ParameterRequest dto);

    ParameterResponse toDTO(Parameter entity);

    @BeanMapping(ignoreByDefault = false)
    void updateEntityFromDto(ParameterRequest dto, @MappingTarget Parameter entity);
}
