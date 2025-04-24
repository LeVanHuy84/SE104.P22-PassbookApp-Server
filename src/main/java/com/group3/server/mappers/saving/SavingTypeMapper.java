package com.group3.server.mappers.saving;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.group3.server.dtos.saving.SavingTicketResponse;
import com.group3.server.dtos.saving.SavingTypeRequest;
import com.group3.server.mappers.GenericMapper;
import com.group3.server.models.saving.SavingType;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SavingTypeMapper extends GenericMapper<SavingTypeRequest, SavingType, SavingTicketResponse> {

}
