package com.group3.server.repositories.saving;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group3.server.models.saving.WithdrawalTicket;

public interface WithdrawalTicketRepository extends JpaRepository<WithdrawalTicket, Long> {

}
