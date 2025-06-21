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

    // Chạy lúc 00:01 sáng mỗi tháng
    //@Scheduled(cron = "0 1 0 1 * ?")
    @Scheduled(cron = "0 44 22 * * ?")
    public void runDailyScheduler() {
        LocalDate yesterday = LocalDate.now().minusDays(1); // Báo cáo cho ngày hôm qua
        reportGeneratorService.createMonthlyReport(yesterday);
    }
}
