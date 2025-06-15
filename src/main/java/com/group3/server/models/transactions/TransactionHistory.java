package com.group3.server.models.transactions;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.group3.server.models.BaseModel;
import com.group3.server.models.auth.User;
import com.group3.server.models.transactions.enums.TransactionType;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="transaction_histories")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionHistory extends BaseModel<Long>{

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;  

    private BigDecimal amount;
    private BigDecimal remainingBalance;
    private TransactionType transactionType;
}
