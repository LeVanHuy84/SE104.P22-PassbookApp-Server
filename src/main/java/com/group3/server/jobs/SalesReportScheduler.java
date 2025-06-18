package com.group3.server.jobs;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.group3.server.services.report.ReportGeneratorService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class SalesReportScheduler {

    private final ReportGeneratorService reportGeneratorService;

    // Chạy lúc 00:01 sáng mỗi ngày
    @Scheduled(cron = "0 1 0 * * *")
    public void runDailyScheduler() {
        LocalDate yesterday = LocalDate.now().minusDays(1); // Báo cáo cho ngày hôm qua
        reportGeneratorService.createDailyReport(yesterday);
    }

    @Scheduled(cron = "0 10 0 1 * *")
    public void runMonthlyScheduler() {
        LocalDate lastMonth = LocalDate.now().minusMonths(1); // Lấy tháng trước
        // Lấy tháng và năm từ ngày tháng trước
        int month = lastMonth.getMonthValue();
        int year = lastMonth.getYear();
        reportGeneratorService.createMonthlyReport(month, year);
    }
}
