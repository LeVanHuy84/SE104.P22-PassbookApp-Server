package com.group3.server.dtos.saving;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavingTypeResponse {
    private Long id;
    private String typeName;
    private int duration;
    private BigDecimal interestRate;
}
