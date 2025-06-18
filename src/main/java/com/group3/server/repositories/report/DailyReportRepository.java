package com.group3.server.repositories.report;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group3.server.models.reports.DailyReport;

public interface DailyReportRepository extends JpaRepository<DailyReport, LocalDate>{
    List<DailyReport> findAllByReportDateBetween(LocalDate startDate, LocalDate endDate);
}
