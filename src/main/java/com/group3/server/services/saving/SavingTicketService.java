package com.group3.server.services.saving;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.group3.server.dtos.Filter.SavingTicketFilter;
import com.group3.server.dtos.Specification.SavingTicketSpecification;
import com.group3.server.dtos.saving.SavingTicketRequest;
import com.group3.server.dtos.saving.SavingTicketResponse;
import com.group3.server.mappers.saving.SavingTicketMapper;
import com.group3.server.models.auth.User;
import com.group3.server.models.saving.SavingTicket;
import com.group3.server.models.saving.SavingType;
import com.group3.server.models.transactions.enums.TransactionType;
import com.group3.server.repositories.auth.UserRepository;
import com.group3.server.repositories.saving.SavingTicketRepository;
import com.group3.server.repositories.saving.SavingTypeRepository;
import com.group3.server.repositories.system.ParameterRepository;
import com.group3.server.services.transaction.TransactionService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SavingTicketService {
    private final SavingTicketRepository savingTicketRepository;
    private final SavingTicketMapper savingTicketMapper;
    private final UserRepository userRepository;
    private final SavingTypeRepository savingTypeRepository;
    private final ParameterRepository parameterRepository;
    private final TransactionService transactionService;

    public Page<SavingTicketResponse> getSavingTickets(SavingTicketFilter filter, Pageable pageable) {
        try {
            Specification<SavingTicket> specification = SavingTicketSpecification.withFilter(filter);
            Page<SavingTicket> tickets = savingTicketRepository.findAll(specification, pageable);
            return tickets.map(savingTicketMapper::toDTO);
        } catch (RuntimeException e) {
            throw new RuntimeException("Lỗi truy cập danh sách phiếu gửi tiết kiệm", e);
        }
    }

    @Transactional
    public SavingTicketResponse createSavingTicket(SavingTicketRequest request) {
        try {
            // B2: Lấy số dư tài khoản
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
            BigDecimal balance = user.getBalance();

            // B3: Đọc loại hình tiết kiệm
            SavingType savingType = savingTypeRepository.findById(request.getSavingTypeId())
                    .orElseThrow(() -> new RuntimeException("Loại tiết kiệm không tồn tại"));

            // B4: Kiểm tra loại hình tiết kiệm hợp lệ
            // (thực ra ở trên findById đã throw nếu không tìm thấy rồi)

            // B5: Kiểm tra số tiền gửi
            BigDecimal minSavingAmount = parameterRepository.findById(1L).orElseThrow().getMinSavingAmount();
            if (request.getAmount().compareTo(minSavingAmount) < 0) {
                throw new RuntimeException("Số tiền gửi tối thiểu là: " + minSavingAmount + " VNĐ");
            }
            if (request.getAmount().compareTo(balance) > 0) {
                throw new RuntimeException("Tài khoản không đủ số dư để gửi tiết kiệm");
            }

            // B7: Set thông tin cho saving ticket
            SavingTicket ticket = savingTicketMapper.toEntity(request);
            ticket.setUser(user);
            ticket.setActive(true);
            ticket.setBalance(request.getAmount());
            ticket.setSavingType(savingType);
            ticket.setInterestRate(savingType.getInterestRate());
            ticket.setDuration(savingType.getDuration());

            // Tạo phiếu giao dịch
            transactionService.createTransaction(request.getAmount(), request.getUserId(), TransactionType.SAVE);

            // B8: Lưu saving ticket
            SavingTicket saved = savingTicketRepository.saveAndFlush(ticket);

            // B9: Tính ngày đáo hạn (sau khi createdAt được gán)
            LocalDate maturityDate = saved.getCreatedAt().toLocalDate().plusMonths(saved.getDuration());
            saved.setMaturityDate(maturityDate);

            return savingTicketMapper.toDTO(saved);
        } catch (RuntimeException e) {
            throw new RuntimeException("Lỗi: " + e.getMessage(), e);
        }
    }
}
