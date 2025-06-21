package com.group3.server.controllers.report;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group3.server.dtos.report.MonthlyReportResponse;
import com.group3.server.services.report.ReportService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    // Endpoint dành cho staff
    @GetMapping("/list-month")
    @PreAuthorize("hasAuthority('VIEW_REPORTS')")
    public ResponseEntity<List<MonthlyReportResponse>> getMonthlyReports(
            @RequestParam int fromYear, @RequestParam int fromMonth,
            @RequestParam int toYear, @RequestParam int toMonth) {
        return ResponseEntity.ok(reportService.getMonthlyReports(fromYear, fromMonth, toYear, toMonth));
    }

    @GetMapping("/monthly")
    @PreAuthorize("hasAuthority('VIEW_REPORTS')")
    public ResponseEntity<MonthlyReportResponse> getMonthlyReport(
            @RequestParam int year, @RequestParam int month) {
        LocalDate monthDate = LocalDate.of(year, month, 1);
        return ResponseEntity.ok(reportService.getReportByMonth(monthDate));
    }
}
