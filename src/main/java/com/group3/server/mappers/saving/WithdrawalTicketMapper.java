package com.group3.server.mappers.saving;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.group3.server.dtos.saving.WithdrawalTicketRequest;
import com.group3.server.dtos.saving.WithdrawalTicketResponse;
import com.group3.server.mappers.GenericMapper;
import com.group3.server.models.saving.WithdrawalTicket;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WithdrawalTicketMapper extends GenericMapper<WithdrawalTicketRequest, WithdrawalTicket, WithdrawalTicketResponse> {

}
