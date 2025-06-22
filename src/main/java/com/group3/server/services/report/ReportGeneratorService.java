package com.group3.server.services.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.group3.server.models.reports.MonthlyReportDetail;
import com.group3.server.models.saving.SavingTicket;
import com.group3.server.models.saving.SavingType;
import com.group3.server.models.saving.WithdrawalTicket;
import com.group3.server.repositories.report.MonthlyReportRepository;
import com.group3.server.repositories.saving.SavingTicketRepository;
import com.group3.server.repositories.saving.SavingTypeRepository;
import com.group3.server.repositories.saving.WithdrawalTicketRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportGeneratorService {

        private final MonthlyReportRepository monthlyReportRepository;
        private final SavingTicketRepository savingTicketRepository;
        private final WithdrawalTicketRepository withdrawalTicketRepository;
        private final SavingTypeRepository savingTypeRepository;

        @Transactional
        public void createMonthlyReport(LocalDate reportDate) {
                LocalDate startOfMonth = reportDate.withDayOfMonth(1);
                LocalDateTime from = startOfMonth.atStartOfDay();
                LocalDateTime to = startOfMonth.withDayOfMonth(reportDate.lengthOfMonth()).atTime(LocalTime.MAX);

                // 1. Lấy tất cả SavingTicket trong tháng
                List<SavingTicket> savingTickets = savingTicketRepository.findAllByCreatedAtBetween(from, to);

                // 2. Lấy tất cả WithdrawalTicket trong tháng
                List<WithdrawalTicket> withdrawalTickets = withdrawalTicketRepository.findAllByCreatedAtBetween(from,
                                to);

                // 3. Tính totalIncome theo savingType
                Map<SavingType, BigDecimal> incomeByType = savingTickets.stream()
                                .collect(Collectors.groupingBy(
                                                SavingTicket::getSavingType,
                                                Collectors.mapping(SavingTicket::getAmount,
                                                                Collectors.reducing(BigDecimal.ZERO,
                                                                                BigDecimal::add))));

                // 4. Tính totalExpense theo savingType (thông qua savingTicket)
                Map<SavingType, BigDecimal> expenseByType = withdrawalTickets.stream()
                                .collect(Collectors.groupingBy(
                                                wt -> wt.getSavingTicket().getSavingType(),
                                                Collectors.mapping(WithdrawalTicket::getActualAmount,
                                                                Collectors.reducing(BigDecimal.ZERO,
                                                                                BigDecimal::add))));

                // 5. Lấy toàn bộ savingType từ DB (đảm bảo không thiếu)
                List<SavingType> allSavingTypes = savingTypeRepository.findAll();

                // 6. Tạo list MonthlyReportDetail theo từng savingType
                for (SavingType type : allSavingTypes) {
                        BigDecimal totalIncome = incomeByType.getOrDefault(type, BigDecimal.ZERO);
                        BigDecimal totalExpense = expenseByType.getOrDefault(type, BigDecimal.ZERO);
                        BigDecimal difference = totalIncome.subtract(totalExpense);

                        MonthlyReportDetail report = MonthlyReportDetail.builder()
                                .reportMonth(startOfMonth)
                                .savingType(type)
                                .totalIncome(totalIncome)
                                .totalExpense(totalExpense)
                                .difference(difference)
                                .build();

                        monthlyReportRepository.save(report);
                }
        }

}
