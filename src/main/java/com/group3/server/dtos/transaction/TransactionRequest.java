package com.group3.server.dtos.transaction;

import java.math.BigDecimal;

import com.group3.server.models.transactions.enums.TransactionType;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {
    private Long userId;

    @NotNull(message="Amount is required")
    private BigDecimal amount;

    @NotNull(message="Transaction type is required")
    private TransactionType transactionType;
}
