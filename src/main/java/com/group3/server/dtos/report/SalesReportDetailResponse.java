package com.group3.server.dtos.report;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesReportDetailResponse {
    private Long savingTypeId;
    private String savingTypeName;

    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal difference;
}
