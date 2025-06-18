package com.group3.server.models.reports;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "daily_reports")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyReport {
    @Id
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate reportDate;

    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal difference;
}
