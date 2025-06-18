package com.group3.server.services.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.group3.server.models.reports.DailyReport;
import com.group3.server.models.reports.MonthlyReport;
import com.group3.server.models.saving.SavingTicket;
import com.group3.server.models.saving.WithdrawalTicket;
import com.group3.server.repositories.report.DailyReportRepository;
import com.group3.server.repositories.report.MonthlyReportRepository;
import com.group3.server.repositories.saving.SavingTicketRepository;
import com.group3.server.repositories.saving.WithdrawalTicketRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportGeneratorService {

        private final DailyReportRepository dailyReportRepository;
        private final MonthlyReportRepository monthlyReportRepository;
        private final SavingTicketRepository savingTicketRepository;
        private final WithdrawalTicketRepository withdrawalTicketRepository;

        @Transactional
        public void createDailyReport(LocalDate reportDate) {
                LocalDateTime from = reportDate.atStartOfDay();
                LocalDateTime to = reportDate.atTime(LocalTime.MAX);

                List<SavingTicket> savingTickets = savingTicketRepository.findAllByCreatedAtBetween(from, to);
                List<WithdrawalTicket> withdrawalTickets = withdrawalTicketRepository.findAllByCreatedAtBetween(from,
                                to);

                BigDecimal totalIncome = savingTickets.stream()
                                .map(SavingTicket::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalExpense = withdrawalTickets.stream()
                                .map(WithdrawalTicket::getActualAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal difference = totalIncome.subtract(totalExpense);

                DailyReport dailyReport = DailyReport.builder()
                                .reportDate(reportDate)
                                .totalIncome(totalIncome)
                                .totalExpense(totalExpense)
                                .difference(difference)
                                .build();

                dailyReportRepository.save(dailyReport);
                calculateMonthlyReport(dailyReport);
                log.info("Daily report created for date: {}", reportDate);
        }

        @Transactional
        public void calculateMonthlyReport(DailyReport dailyReport) {
                LocalDate startDate = dailyReport.getReportDate().withDayOfMonth(1);

                // Tìm báo cáo tháng, nếu không có thì tạo mới
                MonthlyReport existingReport = monthlyReportRepository.findById(startDate)
                                .orElseGet(() -> {
                                        MonthlyReport newReport = MonthlyReport.builder()
                                                        .reportMonth(startDate)
                                                        .totalIncome(BigDecimal.ZERO)
                                                        .totalExpense(BigDecimal.ZERO)
                                                        .difference(BigDecimal.ZERO)
                                                        .build();

                                        log.info("Monthly report created for month: " + startDate);
                                        return monthlyReportRepository.save(newReport);
                                });

                // Cập nhật thu, chi, chênh lệch
                existingReport.setTotalIncome(existingReport.getTotalIncome().add(dailyReport.getTotalIncome()));
                existingReport.setTotalExpense(existingReport.getTotalExpense().add(dailyReport.getTotalExpense()));
                existingReport.setDifference(
                                existingReport.getTotalIncome().subtract(existingReport.getTotalExpense()));

                // Lưu lại
                monthlyReportRepository.save(existingReport);
        }

}
