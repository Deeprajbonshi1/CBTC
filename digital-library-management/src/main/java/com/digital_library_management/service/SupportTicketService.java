package com.digital_library_management.service;
import com.digital_library_management.entity.SupportTicket;
import com.digital_library_management.entity.User;
import com.digital_library_management.repository.SupportTicketRepository;
import jakarta.transaction.Transactional;
import org.hibernate.ResourceClosedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SupportTicketService {

    @Autowired
    private SupportTicketRepository ticketRepository;

    @Transactional
    public SupportTicket createTicket(Long userId, String subject, String message) {
        SupportTicket ticket = SupportTicket.builder()
                .user(User.builder().id(userId).build())
                .subject(subject)
                .message(message)
                .createdAt(LocalDateTime.now())
                .status(SupportTicket.TicketStatus.OPEN)
                .build();
        return ticketRepository.save(ticket);
    }

    public List<SupportTicket> getOpenTickets() {
        return ticketRepository.findByStatus(SupportTicket.TicketStatus.OPEN.name());
    }

    @Transactional
    public void closeTicket(Long ticketId) {
        SupportTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceClosedException("Ticket not found"));
        ticket.setStatus(SupportTicket.TicketStatus.CLOSED);
        ticketRepository.save(ticket);
    }
}