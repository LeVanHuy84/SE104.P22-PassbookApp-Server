package com.group3.server.services.saving;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.group3.server.dtos.saving.WithdrawalTicketRequest;
import com.group3.server.dtos.saving.WithdrawalTicketResponse;
import com.group3.server.mappers.saving.WithdrawalTicketMapper;
import com.group3.server.models.saving.SavingTicket;
import com.group3.server.models.saving.WithdrawalTicket;
import com.group3.server.models.transactions.enums.TransactionType;
import com.group3.server.repositories.saving.SavingTicketRepository;
import com.group3.server.repositories.saving.SavingTypeRepository;
import com.group3.server.repositories.saving.WithdrawalTicketRepository;
import com.group3.server.services.transaction.TransactionService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WithdrawalTicketService {

    private final WithdrawalTicketRepository withdrawalTicketRepository;
    private final WithdrawalTicketMapper withdrawalTicketMapper;
    private final SavingTicketRepository savingTicketRepository;
    private final SavingTypeRepository savingTypeRepository;
    private final TransactionService transactionService;

    @Transactional
    public WithdrawalTicketResponse createWithdrawalTicket(WithdrawalTicketRequest request) {
        try {

            // B3: Đọc D3 từ bộ nhớ - Tìm phiếu gửi tiết kiệm gốc
            SavingTicket savingTicket = savingTicketRepository.findById(request.getSavingTicketId())
                    .orElseThrow(() -> new RuntimeException("Phiếu gửi tiết kiệm không tồn tại"));

            if(savingTicket.getUser().isActive() == false) {
                throw new RuntimeException("Tài khoản người dùng đã bị khóa");
            }

            // B4: Kiểm tra trạng thái phiếu gửi đang hoạt động
            if (!savingTicket.isActive()) {
                throw new RuntimeException("Phiếu gửi tiết kiệm đã đóng, không thể rút tiền");
            }

            BigDecimal interest;
            LocalDate withdrawalDate = LocalDate.now(); // Ngày rút tiền
            LocalDate openDate = savingTicket.getCreatedAt().toLocalDate();
            BigDecimal balance = savingTicket.getBalance();
            BigDecimal updatedBalance;
            BigDecimal nonTermInterestRate = savingTypeRepository.findByDuration(0).getInterestRate(); // Lãi suất không

            updatedBalance = balance.subtract(request.getWithdrawalAmount());

            // B5: Kiểm tra loại tiết kiệm
            if (savingTicket.getDuration() != 0) { // B5.1: Có kỳ hạn
                LocalDate maturityDate = savingTicket.getMaturityDate();
                if (withdrawalDate.isBefore(maturityDate)) {
                    // B5.1.1: Ngày rút trước hạn -> tính theo không kỳ hạn
                    long daysSent = ChronoUnit.DAYS.between(openDate, withdrawalDate); // B5.1.2
                    interest = request.getWithdrawalAmount()
                            .multiply(BigDecimal.valueOf(daysSent))
                            .divide(BigDecimal.valueOf(360), 6, RoundingMode.HALF_UP)
                            .multiply(nonTermInterestRate); // B5.1.3
                    // Đến B6
                } else {
                    // B5.1.5: Rút sau ngày đáo hạn -> tính đủ lãi
                    // Lãi suất kỳ hạn
                    interest = request.getWithdrawalAmount().multiply(savingTicket.getInterestRate());
                }
            } else {
                // B5.2: Không kỳ hạn
                long daysSent = ChronoUnit.DAYS.between(openDate, withdrawalDate); // B5.2.1
                BigDecimal interestRate = savingTicket.getInterestRate();
                interest = request.getWithdrawalAmount()
                        .multiply(BigDecimal.valueOf(daysSent))
                        .divide(BigDecimal.valueOf(360), 6, RoundingMode.HALF_UP)
                        .multiply(interestRate); // B5.2.2
            }

            savingTicket.setBalance(updatedBalance);
            BigDecimal actualAmount = request.getWithdrawalAmount().add(interest);

            // B8: Nếu không thỏa 1 trong các điều kiện thì qua B11 (đã xử lý ở trên bằng
            // throw)

            // Kiểm tra nếu số dư còn lại bằng 0 -> trạng thái = đóng
            if (updatedBalance.compareTo(BigDecimal.ZERO) == 0) {
                savingTicket.setActive(false); // Đóng phiếu gửi
            } else if (updatedBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Số tiền rút vượt quá số dư hiện tại");
            }

            // B9: Lưu D4 xuống bộ nhớ phụ (lưu WithdrawalTicket vào DB)
            WithdrawalTicket ticket = withdrawalTicketMapper.toEntity(request);
            ticket.setSavingTicket(savingTicket);
            ticket.setActualAmount(actualAmount);

            WithdrawalTicket savedTicket = withdrawalTicketRepository.save(ticket);

            // Tạo phiếu giao dịch chuyển tiền vào tài khoản
            transactionService.createTransaction(actualAmount, savingTicket.getUser().getId(),
                    TransactionType.WITHDRAW_SAVING);

            // B10: Xuất D5 ra máy in (ở đây có thể hiểu là trả về DTO cho client xử lý xuất
            // phiếu)
            return withdrawalTicketMapper.toDTO(savedTicket);

        } catch (RuntimeException e) {
            throw new RuntimeException("Lỗi: " + e.getMessage());
        }
        // B11 + B12: Đóng kết nối + Kết thúc (được @Transactional quản lý tự động)
    }
}
