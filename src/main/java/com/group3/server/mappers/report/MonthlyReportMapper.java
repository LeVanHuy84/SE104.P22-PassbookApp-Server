package com.group3.server.mappers.report;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.group3.server.dtos.report.MonthlyReportResponse;
import com.group3.server.models.reports.MonthlyReport;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MonthlyReportMapper {
    MonthlyReportResponse toDTO(MonthlyReport entity);
    List<MonthlyReportResponse> toDTOs(List<MonthlyReport> entities);
}
