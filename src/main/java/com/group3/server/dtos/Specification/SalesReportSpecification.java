package com.group3.server.dtos.Specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.group3.server.dtos.Filter.SalesReportFilter;
import com.group3.server.models.reports.SalesReport;

public class SalesReportSpecification {

    public static Specification<SalesReport> withFilter(SalesReportFilter filter) {
        return Specification
                .where(hasBetweenDate(filter.getStartDate(), filter.getEndDate()));
    }

    // private static Specification<SalesReport> hasSavingTypeId(Long savingTypeId) {
    //     return (root, query, cb) -> {
    //         if (savingTypeId == null) return cb.conjunction();
    //         // Giả sử field `salesReportDetails` là một List chứa các detail, mỗi detail có savingType
    //         // Lọc bằng JOIN (subquery hoặc join fetch tuỳ JPQL hoặc Criteria API)
    //         return cb.equal(
    //             root.join("salesReportDetails").get("savingType").get("id"),
    //             savingTypeId
    //         );
    //     };
    // }

    private static Specification<SalesReport> hasBetweenDate(LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            if (startDate == null && endDate == null) return cb.conjunction();
            if (startDate != null && endDate != null) {
                return cb.between(root.get("reportDate"), startDate, endDate);
            }
            if (startDate != null) {
                return cb.greaterThanOrEqualTo(root.get("reportDate"), startDate);
            }
            return cb.lessThanOrEqualTo(root.get("reportDate"), endDate);
        };
    }
}