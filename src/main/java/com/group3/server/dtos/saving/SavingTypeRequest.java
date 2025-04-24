package com.group3.server.dtos.saving;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavingTypeRequest {
    @NotBlank(message = "Type name is required")
    private String typeName;

    @NotNull(message = "Duration is required")
    private int duration;

    @NotNull(message = "Interest rate is required")
    private BigDecimal interestRate;
}
