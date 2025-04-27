package com.group3.server.models.system;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "parameters")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parameter {
    @Id
    @Builder.Default
    private Long id = 1L;

    private int minAge;
    private BigDecimal minTransactionAmount;
    private BigDecimal maxTransactionAmount;
    private BigDecimal minSavingAmount;
}
