package com.group3.server.services.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.group3.server.dtos.report.SalesReportResponse;
import com.group3.server.mappers.report.SalesReportMapper;
import com.group3.server.models.reports.SalesReport;
import com.group3.server.models.reports.SalesReportDetail;
import com.group3.server.models.reports.SalesReportDetailId;
import com.group3.server.models.saving.SavingTicket;
import com.group3.server.models.saving.SavingType;
import com.group3.server.models.saving.WithdrawalTicket;
import com.group3.server.repositories.report.SalesReportDetailRepository;
import com.group3.server.repositories.report.SalesReportRepository;
import com.group3.server.repositories.saving.SavingTicketRepository;
import com.group3.server.repositories.saving.SavingTypeRepository;
import com.group3.server.repositories.saving.WithdrawalTicketRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalesReportService {

    private final SalesReportRepository salesReportRepository;
    private final SalesReportDetailRepository salesReportDetailRepository;
    private final SavingTicketRepository savingTicketRepository;
    private final WithdrawalTicketRepository withdrawalTicketRepository;
    private final SavingTypeRepository savingTypeRepository;

    private final SalesReportMapper salesReportMapper;

    public List<SalesReportResponse> getSalesReports(int month, int year) {
        try {
            LocalDate startDate = LocalDate.of(year, month, 1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
            return salesReportMapper.toDTOs(salesReportRepository.findAllByReportDateBetween(startDate, endDate));
        } catch (RuntimeException e) {
            log.error("Error fetching sales-report", e);
            throw new RuntimeException("Lỗi truy cập báo cáo", e);
        }
    }

    @Transactional
    public void createDailyReport(LocalDate reportDate) {
        LocalDateTime from = reportDate.atStartOfDay();
        LocalDateTime to = reportDate.atTime(LocalTime.MAX);
    
        // Lấy toàn bộ phiếu gửi và rút trong ngày
        List<SavingTicket> savingTickets = savingTicketRepository.findAllByCreatedAtBetween(from, to);
        List<WithdrawalTicket> withdrawalTickets = withdrawalTicketRepository.findAllByCreatedAtBetween(from, to);
    
        // Gom thu theo loại sổ
        Map<Long, BigDecimal> incomeByType = savingTickets.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getSavingType().getId(),
                        Collectors.mapping(SavingTicket::getAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
    
        // Gom chi theo loại sổ
        Map<Long, BigDecimal> expenseByType = withdrawalTickets.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getSavingTicket().getSavingType().getId(),
                        Collectors.mapping(WithdrawalTicket::getActualAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
    
        // Lấy tất cả loại sổ tiết kiệm hiện có
        List<SavingType> savingTypes = savingTypeRepository.findAll();
    
        SalesReport report = new SalesReport();
        report.setReportDate(reportDate);
        report.setTotalIncome(BigDecimal.ZERO);
        report.setTotalExpense(BigDecimal.ZERO);
        report.setDifference(BigDecimal.ZERO);
        salesReportRepository.save(report); // cần lưu để dùng foreign key
    
        // Duyệt toàn bộ loại sổ
        for (SavingType savingType : savingTypes) {
            Long typeId = savingType.getId();
    
            BigDecimal income = incomeByType.getOrDefault(typeId, BigDecimal.ZERO);
            BigDecimal expense = expenseByType.getOrDefault(typeId, BigDecimal.ZERO);
            
            SalesReportDetail detail = createOrUpdateDetail(report, savingType, income, expense);
            salesReportDetailRepository.save(detail);
    
            // Cộng dồn vào tổng của report
            report.setTotalIncome(report.getTotalIncome().add(income));
            report.setTotalExpense(report.getTotalExpense().add(expense));
        }
    
        // Cập nhật chênh lệch cuối cùng
        report.setDifference(report.getTotalIncome().subtract(report.getTotalExpense()));
        salesReportRepository.save(report); // cập nhật lần cuối
    }

    private SalesReportDetail createOrUpdateDetail(SalesReport report, SavingType savingType,
        BigDecimal incomeDelta, BigDecimal expenseDelta) {

        SalesReportDetail detail = salesReportDetailRepository
                .findBySalesReportAndSavingType(report, savingType)
                .orElseGet(() -> {
                    SalesReportDetail newDetail = new SalesReportDetail();

                    // Tạo composite ID
                    SalesReportDetailId id = new SalesReportDetailId(
                            savingType.getId(),
                            report.getId());
                    newDetail.setId(id);

                    // Gán các mối quan hệ
                    newDetail.setSalesReport(report);
                    newDetail.setSavingType(savingType);

                    // Khởi tạo số liệu ban đầu
                    newDetail.setTotalIncome(BigDecimal.ZERO);
                    newDetail.setTotalExpense(BigDecimal.ZERO);
                    newDetail.setDifference(BigDecimal.ZERO);

                    return newDetail;
                });

        // Cộng dồn thu chi
        detail.setTotalIncome(detail.getTotalIncome().add(incomeDelta));
        detail.setTotalExpense(detail.getTotalExpense().add(expenseDelta));
        detail.setDifference(detail.getTotalIncome().subtract(detail.getTotalExpense()));

        return detail;
    }

}
