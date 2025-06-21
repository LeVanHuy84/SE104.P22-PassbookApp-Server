package com.group3.server.dtos.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.jpa.domain.Specification;

import com.group3.server.dtos.Filter.TransactionFilter;
import com.group3.server.models.transactions.TransactionHistory;
import com.group3.server.models.transactions.enums.TransactionType;

import jakarta.persistence.criteria.Path;

public class TransactionSpecification {

    public static Specification<TransactionHistory> withFilter(TransactionFilter filter) {
        return Specification
                .where(hasCitizenID(filter.getCitizenID()))
                .and(hasTransactionType(filter.getTransactionType()))
                .and(hasAmount(filter.getAmount()))
                .and(hasBetweenDate(filter.getStartDate(), filter.getEndDate()));
    }

    private static Specification<TransactionHistory> hasCitizenID(String citizenID) {
        return (root, query, cb) -> {
            if (citizenID == null || citizenID.isBlank()) return cb.conjunction();
            return cb.equal(root.get("user").get("citizenID"), citizenID);
        };
    }

    private static Specification<TransactionHistory> hasTransactionType(TransactionType transactionType) {
        return (root, query, cb) -> {
            if (transactionType == null) return cb.conjunction();
            return cb.equal(root.get("transactionType"), transactionType);
        };
    }

    private static Specification<TransactionHistory> hasAmount(BigDecimal amount) {
        return (root, query, cb) -> {
            if (amount == null) return cb.conjunction();
            return cb.equal(root.get("amount"), amount);
        };
    }
    
    @SuppressWarnings("null")
    private static Specification<TransactionHistory> hasBetweenDate(LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            if (startDate == null && endDate == null) return cb.conjunction();

            Path<LocalDateTime> createdAt = root.get("createdAt");

            if (startDate != null && endDate != null) {
                return cb.between(
                    createdAt,
                    startDate.atStartOfDay(),
                    endDate.atTime(LocalTime.MAX)
                );
            }
            if (startDate != null) {
                return cb.greaterThanOrEqualTo(createdAt, startDate.atStartOfDay());
            }
            return cb.lessThanOrEqualTo(createdAt, endDate.atTime(LocalTime.MAX));
        };
    }
}
