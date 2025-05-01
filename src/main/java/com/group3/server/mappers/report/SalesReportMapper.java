package com.group3.server.mappers.report;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.group3.server.dtos.report.SalesReportResponse;
import com.group3.server.models.reports.SalesReport;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {SalesReportDetailMapper.class})
public interface SalesReportMapper {
    SalesReportResponse toDTO(SalesReport entity);
    List<SalesReportResponse> toDTOs(List<SalesReport> entities);
}
