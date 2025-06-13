package com.group3.server.controllers.report;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group3.server.dtos.Filter.SalesReportFilter;
import com.group3.server.dtos.report.SalesReportResponse;
import com.group3.server.dtos.responses.PageResponse;
import com.group3.server.services.report.SalesReportService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/sales-reports")
@RequiredArgsConstructor
public class SalesReportController {
    private final SalesReportService salesReportService;

    // Endpoint dành cho staff
    @GetMapping
    public ResponseEntity<PageResponse<SalesReportResponse>> getSalesReports(
            @ModelAttribute SalesReportFilter filter,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            @RequestParam(defaultValue = "asc", required = false) String order) {

        Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<SalesReportResponse> reports = salesReportService.getSalesReports(filter, pageable);

        PageResponse<SalesReportResponse> response = PageResponse.<SalesReportResponse>builder()
                .content(reports.getContent())
                .page(reports.getNumber())
                .size(reports.getSize())
                .totalElements(reports.getTotalElements())
                .totalPages(reports.getTotalPages())
                .last(reports.isLast())
                .first(reports.isFirst())
                .build();

        return ResponseEntity.ok(response);
    }
}
