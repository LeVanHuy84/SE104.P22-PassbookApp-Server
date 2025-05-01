package com.group3.server.dtos.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.group3.server.dtos.Filter.SavingTicketFilter;
import com.group3.server.models.saving.SavingTicket;

public class SavingTicketSpecification {

    public static Specification<SavingTicket> withFilter(SavingTicketFilter filter) {
        return Specification
                .where(hasUserId(filter.getUserId()))
                .and(hasSavingTypeId(filter.getSavingTypeId()))
                .and(hasIsActive(filter.getIsActive()))
                .and(hasAmountBetween(filter.getMinAmount(), filter.getMaxAmount()))
                .and(hasBetweenDate(filter.getStartDate(), filter.getEndDate()));
    }

    private static Specification<SavingTicket> hasUserId(Long userId) {
        return (root, query, cb) -> {
            if (userId == null) return cb.conjunction();
            return cb.equal(root.get("user").get("id"), userId);
        };
    }

    private static Specification<SavingTicket> hasSavingTypeId(Long savingTypeId) {
        return (root, query, cb) -> {
            if (savingTypeId == null) return cb.conjunction();
            return cb.equal(root.get("savingType").get("id"), savingTypeId);
        };
    }

    private static Specification<SavingTicket> hasIsActive(Boolean isActive) {
        return (root, query, cb) -> {
            if (isActive == null) return cb.conjunction();
            return cb.equal(root.get("isActive"), isActive);
        };
    }

    private static Specification<SavingTicket> hasAmountBetween(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return cb.conjunction();
            if (min != null && max != null) {
                return cb.between(root.get("amount"), min, max);
            }
            if (min != null) {
                return cb.greaterThanOrEqualTo(root.get("amount"), min);
            }
            return cb.lessThanOrEqualTo(root.get("amount"), max);
        };
    }

    private static Specification<SavingTicket> hasBetweenDate(LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            if (startDate == null && endDate == null) return cb.conjunction();
            if (startDate != null && endDate != null) {
                return cb.between(root.get("createdDate"), startDate, endDate);
            }
            if (startDate != null) {
                return cb.greaterThanOrEqualTo(root.get("createdDate"), startDate);
            }
            return cb.lessThanOrEqualTo(root.get("createdDate"), endDate);
        };
    }
}