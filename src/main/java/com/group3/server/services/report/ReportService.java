package com.group3.server.services.report;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.group3.server.dtos.report.DailyReportResponse;
import com.group3.server.dtos.report.MonthlyReportResponse;
import com.group3.server.mappers.report.DailyReportMapper;
import com.group3.server.mappers.report.MonthlyReportMapper;
import com.group3.server.models.reports.DailyReport;
import com.group3.server.models.reports.MonthlyReport;
import com.group3.server.repositories.report.DailyReportRepository;
import com.group3.server.repositories.report.MonthlyReportRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final DailyReportRepository dailyReportRepository;
    private final MonthlyReportRepository monthlyReportRepository;
    private final DailyReportMapper dailyReportMapper;
    private final MonthlyReportMapper monthlyReportMapper;

    public List<DailyReportResponse> getDailyReports(int month, int year) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        List<DailyReport> dailyReports = dailyReportRepository.findAllByReportDateBetween(startDate, endDate);
        return dailyReportMapper.toDTOs(dailyReports);
    }

    public List<MonthlyReportResponse> getMonthlyReports(int fromYear, int fromMonth, int toYear, int toMonth) {
        LocalDate startDate = LocalDate.of(fromYear, fromMonth, 1);
        LocalDate endDate = LocalDate.of(toYear, toMonth, 1);
        List<MonthlyReport> monthlyReports = monthlyReportRepository.findAllByReportMonthBetween(startDate, endDate);
        return monthlyReportMapper.toDTOs(monthlyReports);
    }
}
