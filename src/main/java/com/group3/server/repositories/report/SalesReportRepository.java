package com.group3.server.repositories.report;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group3.server.models.reports.SalesReport;

public interface SalesReportRepository extends JpaRepository<SalesReport, Long>{
    Optional<SalesReport> findByReportDate(LocalDate reportDate);
    List<SalesReport> findAllByReportDateBetween(LocalDate startDate, LocalDate endDate);
}
