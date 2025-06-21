package com.group3.server.controllers.saving;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group3.server.dtos.saving.WithdrawalTicketRequest;
import com.group3.server.dtos.saving.WithdrawalTicketResponse;
import com.group3.server.services.saving.WithdrawalTicketService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/withdrawal-tickets")
@RequiredArgsConstructor
public class WithdrawalController {
    private final WithdrawalTicketService withdrawalTicketService;

    // Endpoint dành cho customer/staff
    // Đối với customer, userId sẽ được lấy từ Id của user
    // Đối với staff
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_WITHDRAWALTICKET')")
    public ResponseEntity<WithdrawalTicketResponse> createWithdrawalTicket(@RequestBody WithdrawalTicketRequest request) {
        WithdrawalTicketResponse response = withdrawalTicketService.createWithdrawalTicket(request);
        return ResponseEntity.ok(response);
    }
}
