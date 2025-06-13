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
public class SavingTicketRequest {
    @NotNull(message="User id is required")
    private Long userId;

    @NotNull(message="Saving type id is required")
    private Long savingTypeId;

    @NotNull(message="Amount of saving ticket is required")
    private BigDecimal amount;
}
