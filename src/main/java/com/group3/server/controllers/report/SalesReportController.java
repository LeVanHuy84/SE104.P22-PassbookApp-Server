package com.group3.server.controllers.report;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group3.server.dtos.report.SalesReportResponse;
import com.group3.server.services.report.SalesReportService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/sales-reports")
@RequiredArgsConstructor
public class SalesReportController {
    private final SalesReportService salesReportService;

    // Endpoint dành cho staff
    @GetMapping
    public ResponseEntity<List<SalesReportResponse>> getSalesReports(@RequestParam int month, @RequestParam int year) {
        return ResponseEntity.ok(salesReportService.getSalesReports(month, year));
    }
}
