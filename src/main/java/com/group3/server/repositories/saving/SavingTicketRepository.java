package com.group3.server.repositories.saving;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.group3.server.models.saving.SavingTicket;

public interface SavingTicketRepository extends JpaRepository<SavingTicket, Long>, JpaSpecificationExecutor<SavingTicket> {

}
