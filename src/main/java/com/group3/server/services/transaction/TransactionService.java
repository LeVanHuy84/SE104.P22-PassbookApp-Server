package com.group3.server.services.transaction;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.group3.server.dtos.Filter.TransactionFilter;
import com.group3.server.dtos.Specification.TransactionSpecification;
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
            throw new RuntimeException("Lỗi truy cập danh sách lịch sử giao dịch", e);
        }
    }

    @Transactional
    public TransactionResponse createTransaction(BigDecimal amount, Long userId, TransactionType transactionType) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
            if(!user.isActive()) {
                throw new RuntimeException("Tài khoản người dùng đã bị khóa");
            }

            // B3: Đọc D3 từ bộ nhớ
            Parameter parameter = parameterRepository.findById(1L).orElseThrow();
            BigDecimal minTransactionAmount = parameter.getMinTransactionAmount();
            BigDecimal maxTransactionAmount = parameter.getMaxTransactionAmount();

            // B4: Kiểm tra loại giao dịch có hợp lệ không
            // Không cần vì đã có enum TransactionType
            

            // B5: Kiểm tra số tiền tối thiểu
            if (amount.compareTo(minTransactionAmount) < 0) {
                throw new RuntimeException(
                        "Hạn mức giao dịch tối thiểu là: " + minTransactionAmount + " VNĐ");
            } else if (amount.compareTo(maxTransactionAmount) > 0) {
                throw new RuntimeException(
                        "Hạn mức giao dịch tối đa là: " + maxTransactionAmount + " VNĐ");
            }

            // B7: Tính số dư mới
            BigDecimal currentBalance = user.getBalance();
            BigDecimal newBalance;

            switch (transactionType) {
                case DEPOSIT, WITHDRAW_SAVING -> newBalance = currentBalance.add(amount);
                case WITHDRAWAL, SAVE -> {
                    if (currentBalance.compareTo(amount) < 0) {
                        throw new RuntimeException("Số dư không đủ để thực hiện giao dịch");
                    }
                    newBalance = currentBalance.subtract(amount);
                }
                default -> throw new RuntimeException("Loại giao dịch không hợp lệ: " + transactionType);
            }

            // Cập nhật lại số dư user (bạn tự viết trong userService cho chuẩn nhé)
            user.setBalance(newBalance);

            // B8: Lưu D4 xuống bộ nhớ phụ
            userRepository.save(user);
            TransactionHistory transaction = TransactionHistory.builder()
                    .user(user)
                    .amount(amount)
                    .transactionType(transactionType)
                    .remainingBalance(newBalance)
                    .build();
            TransactionHistory saved = transactionHistoryRepository.saveAndFlush(transaction);

            // B9: Xuất D5 ra máy in (ở đây hiểu là trả kết quả ra cho client)
            return transactionMapper.toDTO(saved);

        } catch (RuntimeException e) {
            // B10: Đóng kết nối CSDL tự động khi lỗi xảy ra
            throw new RuntimeException(e.getMessage(), e);
        }
        // B11: Kết thúc (tự động rollback hoặc commit)
    }
}
