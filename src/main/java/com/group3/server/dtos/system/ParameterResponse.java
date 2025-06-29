package com.group3.server.dtos.system;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParameterResponse {
    private int minAge;
    private BigDecimal minTransactionAmount;
    private BigDecimal maxTransactionAmount;
    private BigDecimal minSavingAmount;
}
