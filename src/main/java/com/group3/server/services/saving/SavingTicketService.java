package com.group3.server.services.saving;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.group3.server.dtos.Filter.SavingTicketFilter;
import com.group3.server.dtos.Specification.SavingTicketSpecification;
import com.group3.server.dtos.saving.SavingTicketRequest;
import com.group3.server.dtos.saving.SavingTicketResponse;
import com.group3.server.dtos.transaction.TransactionRequest;
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
            throw new RuntimeException("Error fetching saving tickets", e);
        }
    }

    @Transactional
    public SavingTicketResponse createSavingTicket(SavingTicketRequest request) {
        try {
            // B2: Lấy số dư tài khoản
            User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
            BigDecimal balance = user.getBalance();

            // B3: Đọc loại hình tiết kiệm
            SavingType savingType = savingTypeRepository.findById(request.getSavingTypeId())
                    .orElseThrow(() -> new RuntimeException("Saving type not found"));

            // B4: Kiểm tra loại hình tiết kiệm hợp lệ
            // (thực ra ở trên findById đã throw nếu không tìm thấy rồi)

            // B5: Kiểm tra số tiền gửi
            BigDecimal minSavingAmount = parameterRepository.findById(1L).orElseThrow().getMinSavingAmount();
            if (request.getAmount().compareTo(minSavingAmount) < 0) {
                throw new RuntimeException("Deposit amount must be greater than minimum required: " + minSavingAmount);
            }
            if (request.getAmount().compareTo(balance) > 0) {
                throw new RuntimeException("Insufficient account balance");
            }

            // B7: Set thông tin cho saving ticket
            SavingTicket ticket = savingTicketMapper.toEntity(request);
            ticket.setUser(user);
            ticket.setActive(true);
            ticket.setBalance(request.getAmount());
            ticket.setMaturityDate(request.getStartDate().plusMonths(savingType.getDuration()));
            ticket.setSavingType(savingType);
            ticket.setInterestRate(savingType.getInterestRate());
            ticket.setDuration(savingType.getDuration());

            // Tạo phiếu giao dịch
            TransactionRequest transaction = TransactionRequest.builder()
                .userId(request.getUserId())
                .amount(request.getAmount())
                .transactionType(TransactionType.SAVE)
                .build();

            transactionService.createTransaction(transaction);

            // B8: Lưu saving ticket
            SavingTicket saved = savingTicketRepository.saveAndFlush(ticket);
            return savingTicketMapper.toDTO(saved);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error creating saving ticket: " + e.getMessage(), e);
        }
    }

    // @Transactional
    // public SavingTicketResponse updateSavingTicket(Long id, SavingTicketRequest request) {
    //     try {
    //         SavingTicket ticket = savingTicketRepository.findById(id)
    //                 .orElseThrow(() -> new RuntimeException("Saving ticket not found"));

    //         if (ticket.getWithdrawalTickets() != null && !ticket.getWithdrawalTickets().isEmpty()) {
    //             throw new RuntimeException(
    //                     "Saving ticket cannot be deleted because it has withdrawal tickets associated with it.");
    //         }

    //         if(request.getSavingTypeId().compareTo(ticket.getSavingType().getId()) != 0) {
    //             throw new RuntimeException("Saving type cannot alter");
    //         }

    //         BigDecimal balance = userService.getUserBalance();
    //         BigDecimal minSavingAmount = parameterRepository.findById(1L).orElseThrow().getMinSavingAmount();
    //         BigDecimal difference = request.getAmount().subtract(ticket.getAmount());

    //         if (request.getAmount().compareTo(minSavingAmount) < 0) {
    //             throw new RuntimeException(
    //                     "Deposit amount must be greater than minimum required: " + minSavingAmount);
    //         }
    //         if (difference.compareTo(balance) > 0) {
    //             throw new RuntimeException("Insufficient account balance");
    //         }

    //         // Cập nhật các trường
    //         savingTicketMapper.updateEntityFromDto(request, ticket);
    //         ticket.setActive(true);
    //         ticket.setBalance(request.getAmount());
    //         ticket.setMaturityDate(request.getStartDate().plusMonths(ticket.getDuration()));

    //         return savingTicketMapper.toDTO(savingTicketRepository.save(ticket));
    //     } catch (RuntimeException e) {
    //         throw new RuntimeException("Error updating saving ticket: " + e.getMessage(), e);
    //     }
    // }

    @Transactional
    public void setSavingTicketActive(Long id, boolean isActive) {
        try {
            SavingTicket savingTicket = savingTicketRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Saving ticket not found"));

            savingTicket.setActive(isActive);

            savingTicketRepository.save(savingTicket);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error updating menu status" + e.getMessage());
        }
    }

    // @Transactional
    // public void deleteSavingTicket(Long id) {
    //     try {
    //         SavingTicket savingTicket = savingTicketRepository.findById(id)
    //                 .orElseThrow(() -> new RuntimeException("Saving ticket not found"));

    //         if (savingTicket.getWithdrawalTickets() != null && !savingTicket.getWithdrawalTickets().isEmpty()) {
    //             throw new RuntimeException(
    //                     "Saving ticket cannot be deleted because it has withdrawal tickets associated with it.");
    //         }

    //         savingTicketRepository.delete(savingTicket);

    //     } catch (RuntimeException e) {
    //         throw new RuntimeException("Error deleting menu" + e.getMessage());
    //     }
    // }
}
