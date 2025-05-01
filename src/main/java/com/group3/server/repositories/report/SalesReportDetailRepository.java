package com.group3.server.repositories.report;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group3.server.models.reports.SalesReport;
import com.group3.server.models.reports.SalesReportDetail;
import com.group3.server.models.saving.SavingType;

public interface SalesReportDetailRepository extends JpaRepository<SalesReportDetail, Long> {
    Optional<SalesReportDetail> findBySalesReportAndSavingType(SalesReport report, SavingType savingType);
}
