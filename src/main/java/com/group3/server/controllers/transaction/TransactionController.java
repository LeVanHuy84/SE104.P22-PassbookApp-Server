package com.group3.server.controllers.transaction;

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

import com.group3.server.dtos.Filter.TransactionFilter;
import com.group3.server.dtos.responses.PageResponse;
import com.group3.server.dtos.transaction.TransactionRequest;
import com.group3.server.dtos.transaction.TransactionResponse;
import com.group3.server.services.auth.UserService;
import com.group3.server.services.transaction.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<PageResponse<TransactionResponse>> getAllTransactions(
            @ModelAttribute TransactionFilter filter,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            @RequestParam(defaultValue = "asc", required = false) String order) {

        Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<TransactionResponse> transactions = transactionService.getTransactionHistories(filter, pageable);

        PageResponse<TransactionResponse> response = PageResponse.<TransactionResponse>builder()
                .content(transactions.getContent())
                .page(transactions.getNumber())
                .size(transactions.getSize())
                .totalElements(transactions.getTotalElements())
                .totalPages(transactions.getTotalPages())
                .last(transactions.isLast())
                .first(transactions.isFirst())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer")
    public ResponseEntity<PageResponse<TransactionResponse>> getTransactionsByCurrentUser(
            @ModelAttribute TransactionFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order) {

        Long currentUserId = userService.getCurrentUserId();

        filter.setUserId(currentUserId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(order), sortBy));
        Page<TransactionResponse> transactions = transactionService.getTransactionHistories(filter, pageable);

        PageResponse<TransactionResponse> response = PageResponse.<TransactionResponse>builder()
                .content(transactions.getContent())
                .page(transactions.getNumber())
                .size(transactions.getSize())
                .totalElements(transactions.getTotalElements())
                .totalPages(transactions.getTotalPages())
                .last(transactions.isLast())
                .first(transactions.isFirst())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody TransactionRequest request) {
        TransactionResponse response = transactionService.createTransaction(request);
        return ResponseEntity.ok(response);
    }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
    //     transactionService.deleteTransactionHistory(id);
    //     return ResponseEntity.noContent().build();
    // }
}
