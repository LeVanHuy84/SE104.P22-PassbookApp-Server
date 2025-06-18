package com.group3.server.mappers.report;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.group3.server.dtos.report.DailyReportResponse;
import com.group3.server.models.reports.DailyReport;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DailyReportMapper {
    DailyReportResponse toDTO(DailyReport entity);
    List<DailyReportResponse> toDTOs(List<DailyReport> entities);
}
