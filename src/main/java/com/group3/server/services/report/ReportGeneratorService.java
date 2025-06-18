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
        List<WithdrawalTicket> withdrawalTickets = withdrawalTicketRepository.findAllByCreatedAtBetween(from, to);

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
        log.info("Daily report created for date: {}", reportDate);
    }


    @Transactional
    public void createMonthlyReport(int month, int year) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<SavingTicket> savingTickets = savingTicketRepository.findAllByCreatedAtBetween(
                startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));
        List<WithdrawalTicket> withdrawalTickets = withdrawalTicketRepository.findAllByCreatedAtBetween(
                startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));

        BigDecimal totalIncome = savingTickets.stream()
                .map(SavingTicket::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = withdrawalTickets.stream()
                .map(WithdrawalTicket::getActualAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal difference = totalIncome.subtract(totalExpense);

        MonthlyReport monthlyReport = MonthlyReport.builder()
                .reportMonth(startDate)
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .difference(difference)
                .build();

        monthlyReportRepository.save(monthlyReport);
        log.info("Monthly report created for month: {}-{}", month, year);
    }
}
