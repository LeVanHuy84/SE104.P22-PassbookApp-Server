package com.group3.server.repositories.report;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group3.server.models.reports.MonthlyReport;

public interface MonthlyReportRepository extends JpaRepository<MonthlyReport, LocalDate>{
    List<MonthlyReport> findAllByReportMonthBetween(LocalDate startDate, LocalDate endDate);
}
