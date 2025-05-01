package com.group3.server.repositories.report;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.group3.server.models.reports.SalesReport;

public interface SalesReportRepository extends JpaRepository<SalesReport, Long>, JpaSpecificationExecutor<SalesReport> {
    Optional<SalesReport> findByReportDate(LocalDate reportDate);
}
