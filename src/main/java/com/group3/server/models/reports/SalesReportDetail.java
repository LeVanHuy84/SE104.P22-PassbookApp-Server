package com.group3.server.models.reports;

import java.math.BigDecimal;

import com.group3.server.models.saving.SavingType;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "sales_report_details")
@NoArgsConstructor
@AllArgsConstructor
public class SalesReportDetail {
    @EmbeddedId
    private SalesReportDetailId id;

    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal difference;

    @MapsId("savingTypeId")
    @ManyToOne
    @JoinColumn(name = "saving_type_id")
    private SavingType savingType;

    @MapsId("reportId")
    @ManyToOne
    @JoinColumn(name = "report_id")
    private SalesReport salesReport;
}
