package com.group3.server.mappers.report;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.group3.server.dtos.report.MonthlyReportDetailResponse;
import com.group3.server.models.reports.MonthlyReportDetail;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MonthlyReportDetailMapper {
    @Mapping(target = "savingTypeId", source = "savingType.id")
    @Mapping(target = "savingTypeName", source = "savingType.typeName")
    MonthlyReportDetailResponse toDTO(MonthlyReportDetail entity);
    List<MonthlyReportDetailResponse> toDTOs(List<MonthlyReportDetail> entities);
}
