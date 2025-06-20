package com.group3.server.controllers.transaction;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group3.server.dtos.Filter.TransactionFilter;
import com.group3.server.dtos.responses.PageResponse;
import com.group3.server.dtos.transaction.TransactionResponse;
import com.group3.server.models.transactions.enums.TransactionType;
import com.group3.server.services.transaction.TransactionService;
import com.group3.server.utils.AuthUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    // Endpoint cho staff
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

    // Endpoint cho customer
    @GetMapping("/customer")
    public ResponseEntity<PageResponse<TransactionResponse>> getTransactionsByCurrentUser(
            @ModelAttribute TransactionFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order) {

        String currentCitizenID = AuthUtils.getCurrentCitizenID();

        filter.setCitizenID(currentCitizenID);

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

    // Endpoint cho staff/ customer
    // Đối với customer, userId sẽ được lấy từ Id của user
    // Đối với staff, userId sẽ được truyền vào (có thể dùng find user byName hoặc by citizenID)
    @PostMapping("/deposit/{userId}")
    public ResponseEntity<TransactionResponse> deposit(
        @PathVariable Long userId,        
        @RequestBody BigDecimal amount
    ){
        TransactionResponse response = transactionService.createTransaction(amount, userId, TransactionType.DEPOSIT);
        return ResponseEntity.ok(response);
    }

    // Endpoint cho staff/ customer tương tự như deposit
    @PostMapping("/withdraw/{userId}")
    public ResponseEntity<TransactionResponse> withdrawal(
        @PathVariable Long userId,        
        @RequestBody BigDecimal amount
    ){
        TransactionResponse response = transactionService.createTransaction(amount, userId, TransactionType.WITHDRAWAL);
        return ResponseEntity.ok(response);
    }
}
