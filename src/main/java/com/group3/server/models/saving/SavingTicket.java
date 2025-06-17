package com.group3.server.models.saving;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.group3.server.models.BaseModel;
import com.group3.server.models.auth.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "saving_tickets")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavingTicket extends BaseModel<Long>{

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "saving_type_id", nullable = false)
    @JsonBackReference
    private SavingType savingType;

    @Builder.Default
    @OneToMany(mappedBy = "savingTicket", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    @JsonManagedReference
    private List<WithdrawalTicket> withdrawalTickets = new ArrayList<>();

    @Column(precision = 5, scale = 3) // Tổng 5 chữ số, 3 chữ số sau dấu phẩy
    private BigDecimal interestRate;

    private int duration;
    private BigDecimal amount;
    private BigDecimal balance;
    private LocalDate maturityDate;

    @Builder.Default
    private boolean isActive = true;
}
