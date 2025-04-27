package com.group3.server.services.transaction;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.group3.server.dtos.Filter.TransactionFilter;
import com.group3.server.dtos.Specification.TransactionSpecification;
import com.group3.server.dtos.transaction.TransactionRequest;
import com.group3.server.dtos.transaction.TransactionResponse;
import com.group3.server.mappers.transaction.TransactionMapper;
import com.group3.server.models.auth.User;
import com.group3.server.models.system.Parameter;
import com.group3.server.models.transactions.TransactionHistory;
import com.group3.server.models.transactions.enums.TransactionType;
import com.group3.server.repositories.auth.UserRepository;
import com.group3.server.repositories.system.ParameterRepository;
import com.group3.server.repositories.transactions.TransactionHistoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final TransactionMapper transactionMapper;
    private final UserRepository userRepository;
    private final ParameterRepository parameterRepository;

    public Page<TransactionResponse> getTransactionHistories(TransactionFilter filter, Pageable pageable) {
        try {
            Specification<TransactionHistory> specification = TransactionSpecification.withFilter(filter);
            Page<TransactionHistory> tickets = transactionHistoryRepository.findAll(specification, pageable);
            return tickets.map(transactionMapper::toDTO);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error fetching saving tickets", e);
        }
    }

    @Transactional
    public TransactionResponse createTransaction(TransactionRequest request) {
        try {
            // B3: Đọc D3 từ bộ nhớ
            Parameter parameter = parameterRepository.findById(1L).orElseThrow();
            BigDecimal minTransactionAmount = parameter.getMinTransactionAmount();
            BigDecimal maxTransactionAmount = parameter.getMaxTransactionAmount();

            // B4: Kiểm tra loại giao dịch có hợp lệ không
            if (request.getTransactionType() == null) {
                throw new RuntimeException("Transaction type must not be null");
            }

            boolean isValidType = Arrays.stream(TransactionType.values())
                    .anyMatch(type -> type == request.getTransactionType());
            if (!isValidType) {
                throw new RuntimeException("Invalid transaction type");
            }

            // B5: Kiểm tra số tiền tối thiểu
            if (request.getAmount().compareTo(minTransactionAmount) < 0) {
                throw new RuntimeException(
                        "Transaction amount must be greater than or equal to minimum required amount");
            } else if (request.getAmount().compareTo(maxTransactionAmount) > 0) {
                throw new RuntimeException(
                        "Transaction amount must be less than or equal to maximum required amount");
            }

            // B7: Tính số dư mới
            User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
            BigDecimal currentBalance = user.getBalance();
            BigDecimal newBalance;

            switch (request.getTransactionType()) {
                case DEPOSIT, WITHDRAW_SAVING -> newBalance = currentBalance.add(request.getAmount());
                case WITHDRAWAL, SAVE -> {
                    if (currentBalance.compareTo(request.getAmount()) < 0) {
                        throw new RuntimeException("Insufficient balance for withdrawal");
                    }
                    newBalance = currentBalance.subtract(request.getAmount());
                }
                default -> throw new RuntimeException("Unsupported transaction type");
            }

            // Cập nhật lại số dư user (bạn tự viết trong userService cho chuẩn nhé)
            user.setBalance(newBalance);

            // B8: Lưu D4 xuống bộ nhớ phụ
            userRepository.save(user);
            TransactionHistory transaction = transactionMapper.toEntity(request);
            TransactionHistory saved = transactionHistoryRepository.saveAndFlush(transaction);

            // B9: Xuất D5 ra máy in (ở đây hiểu là trả kết quả ra cho client)
            return transactionMapper.toDTO(saved);

        } catch (RuntimeException e) {
            // B10: Đóng kết nối CSDL tự động khi lỗi xảy ra
            throw new RuntimeException("Error creating transaction: " + e.getMessage(), e);
        }
        // B11: Kết thúc (tự động rollback hoặc commit)
    }

    public void deleteTransactionHistory(Long id) {
        try {
            TransactionHistory transactionHistory = transactionHistoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Transaction history not found"));

            transactionHistoryRepository.delete(transactionHistory);

        } catch (RuntimeException e) {
            throw new RuntimeException("Error deleting transaction history" + e.getMessage());
        }
    }
}
