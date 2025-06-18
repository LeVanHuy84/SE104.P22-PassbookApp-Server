package com.group3.server.mappers.transaction;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.group3.server.dtos.transaction.TransactionResponse;
import com.group3.server.models.transactions.TransactionHistory;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransactionMapper{
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.fullName", target = "userFullname")
    TransactionResponse toDTO(TransactionHistory entity);

    List<TransactionResponse> toDTOs(List<TransactionHistory> entities);
}
