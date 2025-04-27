package com.group3.server.mappers.transaction;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.group3.server.dtos.transaction.TransactionRequest;
import com.group3.server.dtos.transaction.TransactionResponse;
import com.group3.server.mappers.GenericMapper;
import com.group3.server.models.transactions.TransactionHistory;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransactionMapper extends GenericMapper<TransactionRequest, TransactionHistory, TransactionResponse>{
    @Override
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.fullname", target = "userFullname")
    TransactionResponse toDTO(TransactionHistory entity);
}
