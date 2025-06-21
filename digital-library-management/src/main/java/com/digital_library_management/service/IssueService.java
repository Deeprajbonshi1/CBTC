package com.digital_library_management.service;

import com.digital_library_management.entity.*;
import com.digital_library_management.repository.BookRepository;
import com.digital_library_management.repository.FineRepository;
import com.digital_library_management.repository.IssueRepository;
import com.digital_library_management.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.hibernate.ResourceClosedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class IssueService {

    private static final BigDecimal FINE_PER_DAY = new BigDecimal("5"); // ₹5 per day

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private FineRepository fineRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public List<Issue> getIssuesByStatus(Issue.IssueStatus status) {
        return issueRepository.findByStatus(status);
    }

    public List<Issue> getUserIssues(Long userId, Issue.IssueStatus status) {
        return issueRepository.findByUserIdAndStatus(userId, status);
    }


    @Transactional
    public Issue requestIssue(Long userId, Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceClosedException("Book not found"));

        Issue issue = Issue.builder()
                .user(User.builder().id(userId).build())
                .book(book)
                .requestDate(LocalDate.now())
                .status(Issue.IssueStatus.PENDING)
                .build();
        return issueRepository.save(issue);
    }

    @Transactional
    public Issue approveIssue(Long issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new ResourceClosedException("Issue request not found"));

        if (issue.getStatus() != Issue.IssueStatus.PENDING) {
            throw new RuntimeException("Issue is not in PENDING state");
        }

        Book book = issue.getBook();
        if (book.getQuantityAvailable() <= 0) {
            throw new RuntimeException("No copies available");
        }

        // Update book availability
        book.setQuantityAvailable(book.getQuantityAvailable() - 1);
        bookRepository.save(book);

        issue.setIssueDate(LocalDate.now());
        issue.setDueDate(issue.getIssueDate().plusDays(14));
        issue.setStatus(Issue.IssueStatus.ISSUED);
        return issueRepository.save(issue);
    }

    @Transactional
    public Issue requestReturn(Long issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new ResourceClosedException("Issue not found"));
        if (issue.getStatus() != Issue.IssueStatus.ISSUED) {
            throw new RuntimeException("Issue is not in ISSUED state");
        }
        issue.setStatus(Issue.IssueStatus.RETURN_REQUESTED);
        return issueRepository.save(issue);
    }

    @Transactional
    public Issue confirmReturn(Long issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new ResourceClosedException("Issue not found"));
        if (issue.getStatus() != Issue.IssueStatus.RETURN_REQUESTED) {
            throw new RuntimeException("Issue is not in RETURN_REQUESTED state");
        }

        Book book = issue.getBook();
        book.setQuantityAvailable(book.getQuantityAvailable() + 1);
        bookRepository.save(book);

        // Calculate fine if overdue
        LocalDate today = LocalDate.now();
        long daysLate = ChronoUnit.DAYS.between(issue.getDueDate(), today);
        if (daysLate > 0) {
            BigDecimal amount = FINE_PER_DAY.multiply(BigDecimal.valueOf(daysLate));
            Fine fine = Fine.builder()
                    .issue(issue)
                    .amount(amount)
                    .paid(false)
                    .calculatedDate(today)
                    .build();
            fineRepository.save(fine);
        }

        // Check for pending reservations for this book
        List<Reservation> pendingRes = reservationRepository.findByBookIdAndStatusOrderByReservationDateAsc(
                book.getId(), Reservation.ReservationStatus.PENDING);
        if (!pendingRes.isEmpty()) {
            Reservation res = pendingRes.get(0);
            res.setStatus(Reservation.ReservationStatus.FULFILLED);
            reservationRepository.save(res);
            // In a real app, send email notification to res.getUser()
        }

        issue.setStatus(Issue.IssueStatus.RETURNED);
        issue.setReturnDate(today);
        return issueRepository.save(issue);
    }
}