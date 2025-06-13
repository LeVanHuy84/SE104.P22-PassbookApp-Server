package com.group3.server.controllers.saving;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group3.server.dtos.Filter.SavingTicketFilter;
import com.group3.server.dtos.responses.PageResponse;
import com.group3.server.dtos.saving.SavingTicketRequest;
import com.group3.server.dtos.saving.SavingTicketResponse;
import com.group3.server.services.saving.SavingTicketService;
import com.group3.server.utils.AuthUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/saving-tickets")
@RequiredArgsConstructor
public class SavingTicketController {

    private final SavingTicketService savingTicketService;

    //Endpoint dành cho staff
    @GetMapping
    public ResponseEntity<PageResponse<SavingTicketResponse>> getSavingTickets(
            @ModelAttribute SavingTicketFilter filter,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            @RequestParam(defaultValue = "asc", required = false) String order) {

        Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<SavingTicketResponse> tickets = savingTicketService.getSavingTickets(filter, pageable);

        PageResponse<SavingTicketResponse> response = PageResponse.<SavingTicketResponse>builder()
                .content(tickets.getContent())
                .page(tickets.getNumber())
                .size(tickets.getSize())
                .totalElements(tickets.getTotalElements())
                .totalPages(tickets.getTotalPages())
                .last(tickets.isLast())
                .first(tickets.isFirst())
                .build();

        return ResponseEntity.ok(response);
    }

    //Endpoint dành cho customer
    @GetMapping("/customer")
    public ResponseEntity<PageResponse<SavingTicketResponse>> getSavingTicketsForCustomer(
            @ModelAttribute SavingTicketFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order) {

        // Lấy thông tin customer hiện tại
        Long currentUserId = AuthUtils.getCurrentUserId();

        // Bắt buộc filter theo customerId
        filter.setUserId(currentUserId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(order), sortBy));
        Page<SavingTicketResponse> tickets = savingTicketService.getSavingTickets(filter, pageable);

        PageResponse<SavingTicketResponse> response = PageResponse.<SavingTicketResponse>builder()
                .content(tickets.getContent())
                .page(tickets.getNumber())
                .size(tickets.getSize())
                .totalElements(tickets.getTotalElements())
                .totalPages(tickets.getTotalPages())
                .last(tickets.isLast())
                .first(tickets.isFirst())
                .build();

        return ResponseEntity.ok(response);
    }

    // Đối với customer, userId sẽ được lấy từ Id của user
    // Đối với staff, userId sẽ được truyền vào (có thể dùng find user byName hoặc by citizenID)
    @PostMapping
    public ResponseEntity<SavingTicketResponse> createSavingTicket(@RequestBody SavingTicketRequest request) {
        SavingTicketResponse response = savingTicketService.createSavingTicket(request);
        return ResponseEntity.ok(response);
    }

}
