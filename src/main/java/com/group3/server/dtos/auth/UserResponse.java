package com.group3.server.dtos.auth;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String fullname;
    private LocalDate dateOfBirth;
    private String citizenID;
    private String address;
    private BigDecimal balance;
}
