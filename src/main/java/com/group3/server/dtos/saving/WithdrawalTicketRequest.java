package com.group3.server.dtos.saving;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WithdrawalTicketRequest {
    @NotNull(message="Saving ticket id is required")
    private Long savingTicketId;

    @NotNull(message="Withdrawal amount is required")
    private BigDecimal withdrawalAmount;
}
