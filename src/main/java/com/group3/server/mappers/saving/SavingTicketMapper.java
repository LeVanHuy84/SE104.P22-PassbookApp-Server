package com.group3.server.mappers.saving;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.group3.server.dtos.saving.SavingTicketRequest;
import com.group3.server.dtos.saving.SavingTicketResponse;
import com.group3.server.mappers.GenericMapper;
import com.group3.server.models.saving.SavingTicket;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SavingTicketMapper extends GenericMapper<SavingTicketRequest, SavingTicket, SavingTicketResponse> {

    @Mapping(target = "withdrawalTickets", ignore = true)
    @Override
    SavingTicket toEntity(SavingTicketRequest dto);

    @Override
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.fullname", target = "userFullname")
    @Mapping(source = "savingType.id", target = "savingTypeId")
    @Mapping(source = "savingType.typeName", target = "savingTypeName")
    SavingTicketResponse toDTO(SavingTicket entity);
}
