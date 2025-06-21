package com.group3.server.models.reports;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.group3.server.models.saving.SavingType;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "monthly_reports")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyReportDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate reportMonth;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "saving_type_id", nullable = false)
    SavingType savingType;

    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal difference;
}
