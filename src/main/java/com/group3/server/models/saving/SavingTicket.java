package com.group3.server.models.saving;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.group3.server.models.auth.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "saving_tickets")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavingTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saving_type_id", nullable = false)
    @JsonBackReference
    private SavingType savingType;

    @Column(precision = 5, scale = 2) // Tổng 5 chữ số, 2 chữ số sau dấu phẩy
    private BigDecimal interestRate;

    private int duration;
    private BigDecimal amount;
    private LocalDateTime startDate;
    private BigDecimal balance;
    private LocalDateTime maturityDate;

    @Column(nullable = false)
    @Builder.Default
    private boolean isActive = true;
}
