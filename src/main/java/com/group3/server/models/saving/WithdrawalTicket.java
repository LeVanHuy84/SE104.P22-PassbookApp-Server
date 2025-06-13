package com.group3.server.models.saving;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.group3.server.models.BaseModel;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "withdrawal_tickets")
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalTicket extends BaseModel<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saving_ticket_id", nullable = false)
    @JsonBackReference
    private SavingTicket savingTicket;

    private BigDecimal withdrawalAmount;

    private BigDecimal actualAmount;
}
