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
import com.group3.server.services.report.SalesReportService;
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
    private final SalesReportService salesReportService;

    @Transactional
    public WithdrawalTicketResponse createWithdrawalTicket(WithdrawalTicketRequest request) {
        try {

            // B3: Đọc D3 từ bộ nhớ - Tìm phiếu gửi tiết kiệm gốc
            SavingTicket savingTicket = savingTicketRepository.findById(request.getSavingTicketId())
                    .orElseThrow(() -> new RuntimeException("Saving ticket not found"));

            // B4: Kiểm tra trạng thái phiếu gửi đang hoạt động
            if (!savingTicket.isActive()) {
                throw new RuntimeException("Saving ticket is not active");
            }

            BigDecimal interest;
            LocalDate withdrawalDate = request.getWithdrawalDate().toLocalDate();
            LocalDate openDate = savingTicket.getStartDate().toLocalDate();
            BigDecimal balance = savingTicket.getBalance();
            BigDecimal updatedBalance;
            BigDecimal nonTermInterestRate = savingTypeRepository.findByDuration(0).getInterestRate(); // Lãi suất không

            updatedBalance = balance.subtract(request.getWithdrawalAmount());

            // Kiểm tra nếu số dư còn lại bằng 0 -> trạng thái = đóng
            if (updatedBalance.compareTo(BigDecimal.ZERO) == 0) {
                savingTicket.setActive(false); // Đóng phiếu gửi
            } else if (updatedBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Withdrawal amount exceeds balance plus interest");
            }

            // Kiểm tra ngày
            if (request.getWithdrawalDate().toLocalDate().isAfter(LocalDate.now())) {
                throw new RuntimeException("Start date cannot exceed current date");
            } else if (request.getWithdrawalDate().toLocalDate().isBefore(LocalDate.now().minusDays(7))) {
                throw new RuntimeException("Cannot create tickets more than 7 days ago");
            }

            // B5: Kiểm tra loại tiết kiệm
            if (savingTicket.getDuration() != 0) { // B5.1: Có kỳ hạn
                LocalDate maturityDate = savingTicket.getMaturityDate().toLocalDate();
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

            // B9: Lưu D4 xuống bộ nhớ phụ (lưu WithdrawalTicket vào DB)
            WithdrawalTicket ticket = withdrawalTicketMapper.toEntity(request);
            ticket.setSavingTicket(savingTicket);
            ticket.setActualAmount(actualAmount);

            WithdrawalTicket savedTicket = withdrawalTicketRepository.save(ticket);

            // Tạo phiếu giao dịch chuyển tiền vào tài khoản
            transactionService.createTransaction(actualAmount, savingTicket.getUser().getId(),
                    TransactionType.WITHDRAW_SAVING);

            if(!request.getWithdrawalDate().toLocalDate().isEqual(LocalDate.now())) {
                salesReportService.updateReportFromWithdrawalTicket(savedTicket);
            }

            // B10: Xuất D5 ra máy in (ở đây có thể hiểu là trả về DTO cho client xử lý xuất
            // phiếu)
            return withdrawalTicketMapper.toDTO(savedTicket);

        } catch (RuntimeException e) {
            throw new RuntimeException("Error creating withdrawal ticket: " + e.getMessage());
        }
        // B11 + B12: Đóng kết nối + Kết thúc (được @Transactional quản lý tự động)
    }

    // @Transactional
    // public WithdrawalTicketResponse updateWithdrawalTicket(Long id,
    // WithdrawalTicketRequest request) {
    // try {
    // WithdrawalTicket ticket = withdrawalTicketRepository.findById(id)
    // .orElseThrow(() -> new RuntimeException("Withdrawal ticket not found"));

    // withdrawalTicketMapper.updateEntityFromDto(request, ticket);
    // return withdrawalTicketMapper.toDTO(withdrawalTicketRepository.save(ticket));
    // } catch (RuntimeException e) {
    // throw new RuntimeException("Error updating withdrawal ticket: " +
    // e.getMessage());
    // }
    // }

    // public void deleteWithdrawalTicket(Long id) {
    // try {
    // WithdrawalTicket ticket = withdrawalTicketRepository.findById(id)
    // .orElseThrow(() -> new RuntimeException("Withdrawal ticket not found"));

    // withdrawalTicketRepository.delete(ticket);
    // } catch (RuntimeException e) {
    // throw new RuntimeException("Error deleting withdrawal ticket: " +
    // e.getMessage());
    // }
    // }
}
