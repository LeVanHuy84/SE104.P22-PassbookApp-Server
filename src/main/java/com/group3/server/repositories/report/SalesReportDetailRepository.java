package com.group3.server.repositories.report;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group3.server.models.reports.SalesReportDetail;

public interface SalesReportDetailRepository extends JpaRepository<SalesReportDetail, Long> {

}
