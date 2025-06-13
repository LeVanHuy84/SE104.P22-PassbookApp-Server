package com.group3.server.dtos.Specification;

import com.group3.server.dtos.Filter.TransactionFilter;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

import com.group3.server.models.transactions.TransactionHistory;
import com.group3.server.models.transactions.enums.TransactionType;

public class TransactionSpecification {

    public static Specification<TransactionHistory> withFilter(TransactionFilter filter) {
        return Specification
                .where(hasUserId(filter.getUserId()))
                .and(hasTransactionType(filter.getTransactionType()))
                .and(hasBetweenDate(filter.getStartDate(), filter.getEndDate()));
    }

    private static Specification<TransactionHistory> hasUserId(Long userId) {
        return (root, query, cb) -> {
            if (userId == null) return cb.conjunction();
            return cb.equal(root.get("user").get("id"), userId);
        };
    }

    private static Specification<TransactionHistory> hasTransactionType(TransactionType transactionType) {
        return (root, query, cb) -> {
            if (transactionType == null) return cb.conjunction();
            return cb.equal(root.get("transactionType"), transactionType);
        };
    }

    private static Specification<TransactionHistory> hasBetweenDate(LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            if (startDate == null && endDate == null) return cb.conjunction();
            if (startDate != null && endDate != null) {
                return cb.between(root.get("createdAt"), startDate, endDate);
            }
            if (startDate != null) {
                return cb.greaterThanOrEqualTo(root.get("createdAt"), startDate);
            }
            return cb.lessThanOrEqualTo(root.get("createdAt"), endDate);
        };
    }
}
