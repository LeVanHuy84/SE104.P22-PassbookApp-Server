package com.group3.server.models.reports;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "sales_reports")
@NoArgsConstructor
@AllArgsConstructor
public class SalesReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate reportDate;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal difference;

    @OneToMany(mappedBy = "salesReport", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    @JsonManagedReference
    private List<SalesReportDetail> salesReportDetails = new ArrayList<>();
}
