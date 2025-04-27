package com.group3.server.controllers.saving;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group3.server.dtos.Filter.SavingTicketFilter;
import com.group3.server.dtos.responses.PageResponse;
import com.group3.server.dtos.saving.SavingTicketRequest;
import com.group3.server.dtos.saving.SavingTicketResponse;
import com.group3.server.services.auth.UserService;
import com.group3.server.services.saving.SavingTicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/saving-tickets")
@RequiredArgsConstructor
public class SavingTicketController {

    private final SavingTicketService savingTicketService;
    private final UserService userService;

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

    @GetMapping("/customer") // customer
    public ResponseEntity<PageResponse<SavingTicketResponse>> getSavingTicketsForCustomer(
            @ModelAttribute SavingTicketFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order) {

        // Lấy thông tin customer hiện tại
        Long currentUserId = userService.getCurrentUserId();

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

    @PostMapping
    public ResponseEntity<SavingTicketResponse> createSavingTicket(@RequestBody SavingTicketRequest request) {
        SavingTicketResponse response = savingTicketService.createSavingTicket(request);
        return ResponseEntity.ok(response);
    }

    // @PutMapping("/{id}")
    // public ResponseEntity<SavingTicketResponse> updateSavingTicket(@PathVariable Long id,
    //         @RequestBody SavingTicketRequest request) {
    //     SavingTicketResponse response = savingTicketService.updateSavingTicket(id, request);
    //     return ResponseEntity.ok(response);
    // }

    @PatchMapping("/active/{id}")
    public ResponseEntity<Void> setSavingTicketActive(@PathVariable Long id, @RequestBody boolean isActive) {
        savingTicketService.setSavingTicketActive(id, isActive);
        return ResponseEntity.noContent().build();
    }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> deleteSavingTicket(@PathVariable Long id) {
    //     savingTicketService.deleteSavingTicket(id);
    //     return ResponseEntity.noContent().build();
    // }
}
