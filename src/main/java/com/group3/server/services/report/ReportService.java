package com.group3.server.services.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.group3.server.dtos.report.MonthlyReportDetailResponse;
import com.group3.server.dtos.report.MonthlyReportResponse;
import com.group3.server.mappers.report.MonthlyReportDetailMapper;
import com.group3.server.models.reports.MonthlyReportDetail;
import com.group3.server.repositories.report.MonthlyReportRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final MonthlyReportRepository monthlyReportRepository;
    private final MonthlyReportDetailMapper monthlyReportDetailMapper;

    public List<MonthlyReportResponse> getMonthlyReports(int fromYear, int fromMonth, int toYear, int toMonth) {
        List<MonthlyReportResponse> result = new ArrayList<>();

        YearMonth from = YearMonth.of(fromYear, fromMonth);
        YearMonth to = YearMonth.of(toYear, toMonth);

        YearMonth current = from;
        while (!current.isAfter(to)) {
            LocalDate reportMonth = current.atDay(1);
            MonthlyReportResponse report = getReportByMonth(reportMonth);

            // Nếu trong DB không có dữ liệu thì report sẽ rỗng, ta có thể bỏ qua hoặc thêm
            // điều kiện
            if (report.getMonthlyReportDetails() != null && !report.getMonthlyReportDetails().isEmpty()) {
                result.add(report);
            }

            current = current.plusMonths(1);
        }

        return result;
    }

    public MonthlyReportResponse getReportByMonth(LocalDate month) {
        List<MonthlyReportDetail> reports = monthlyReportRepository.findAllByReportMonth(month);

        List<MonthlyReportDetailResponse> dtos = monthlyReportDetailMapper.toDTOs(reports);

        BigDecimal totalIncome = dtos.stream()
                .map(MonthlyReportDetailResponse::getTotalIncome)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = dtos.stream()
                .map(MonthlyReportDetailResponse::getTotalExpense)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal difference = dtos.stream()
                .map(MonthlyReportDetailResponse::getDifference)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return MonthlyReportResponse.builder()
                .reportMonth(month)
                .monthlyReportDetails(dtos)
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .difference(difference)
                .build();
    }

}
