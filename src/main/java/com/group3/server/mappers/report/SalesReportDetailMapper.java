package com.group3.server.mappers.report;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.group3.server.dtos.report.SalesReportDetailResponse;
import com.group3.server.models.reports.SalesReportDetail;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SalesReportDetailMapper {
    SalesReportDetailResponse toDTO(SalesReportDetail entity);
    List<SalesReportDetailResponse> toDTOs(List<SalesReportDetail> entities);
}
