package com.group3.server.models.saving;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "saving_types")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false)
    private String typeName;
    private int duration;
    
    @Column(precision = 6, scale = 4) // Tổng 6 chữ số, 4 chữ số sau dấu phẩy
    private BigDecimal interestRate;

    @Builder.Default
    private boolean isActive = true;

    @Builder.Default
    @OneToMany(mappedBy = "savingType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<SavingTicket> savingTickets = new ArrayList<>();
}
