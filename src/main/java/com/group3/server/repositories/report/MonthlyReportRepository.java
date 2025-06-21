package com.group3.server.repositories.report;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group3.server.models.reports.MonthlyReportDetail;


public interface MonthlyReportRepository extends JpaRepository<MonthlyReportDetail, LocalDate>{
    List<MonthlyReportDetail> findAllByReportMonth(LocalDate month);
    List<MonthlyReportDetail> findAllByReportMonthBetween(LocalDate startDate, LocalDate endDate);
}
