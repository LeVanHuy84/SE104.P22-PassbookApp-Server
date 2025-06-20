package com.group3.server.dtos.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.jpa.domain.Specification;

import com.group3.server.dtos.Filter.SavingTicketFilter;
import com.group3.server.models.saving.SavingTicket;

import jakarta.persistence.criteria.Path;

public class SavingTicketSpecification {

    public static Specification<SavingTicket> withFilter(SavingTicketFilter filter) {
        return Specification
                .where(hasCitizenID(filter.getCitizenID()))
                .and(hasSavingTypeId(filter.getSavingTypeId()))
                .and(hasAmount(filter.getAmount()))
                .and(hasBetweenDate(filter.getStartDate(), filter.getEndDate()));
    }

        private static Specification<SavingTicket> hasCitizenID(String citizenID) {
        return (root, query, cb) -> {
            if (citizenID == null) return cb.conjunction();
            return cb.equal(root.get("user").get("citizenID"), citizenID);
        };
    }

    private static Specification<SavingTicket> hasSavingTypeId(Long savingTypeId) {
        return (root, query, cb) -> {
            if (savingTypeId == null) return cb.conjunction();
            return cb.equal(root.get("savingType").get("id"), savingTypeId);
        };
    }

    private static Specification<SavingTicket> hasAmount(BigDecimal amount) {
        return (root, query, cb) -> {
            if (amount == null) return cb.conjunction();
            return cb.equal(root.get("amount"), amount);
        };
    }

    @SuppressWarnings("null")
    private static Specification<SavingTicket> hasBetweenDate(LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            if (startDate == null && endDate == null) return cb.conjunction();

            Path<LocalDateTime> createdAt = root.get("createdAt");

            if (startDate != null && endDate != null) {
                return cb.between(
                    createdAt,
                    startDate.atStartOfDay(),
                    endDate.atTime(LocalTime.MAX) // tức là 23:59:59.999999999
                );
            }
            if (startDate != null) {
                return cb.greaterThanOrEqualTo(createdAt, startDate.atStartOfDay());
            }
            return cb.lessThanOrEqualTo(createdAt, endDate.atTime(LocalTime.MAX));
        };
    }

}