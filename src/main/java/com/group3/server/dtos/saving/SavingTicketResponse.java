package com.group3.server.dtos.saving;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavingTicketResponse {
    private Long id;

    private Long userId;
    private String citizenId;
    private String userFullname;

    private Long savingTypeId;
    private String savingTypeName;

    private int duration;
    private BigDecimal interestRate;

    private BigDecimal amount;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    private BigDecimal balance;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate maturityDate;

    @Builder.Default
    List<WithdrawalTicketResponse> withdrawalTickets = new ArrayList<>();
}
