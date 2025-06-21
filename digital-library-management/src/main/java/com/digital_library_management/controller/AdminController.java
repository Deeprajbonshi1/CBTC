package com.digital_library_management.controller;

import com.digital_library_management.dto.BookDto;
import com.digital_library_management.entity.Book;
import com.digital_library_management.entity.Issue;
import com.digital_library_management.entity.Reservation;
import com.digital_library_management.service.BookService;
import com.digital_library_management.service.IssueService;
import com.digital_library_management.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private BookService bookService;
    @Autowired
    private IssueService issueService;
    @Autowired
    private ReservationService reservationService;

    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard() {
        return ResponseEntity.ok(Map.of(
                "pendingIssues", issueService.getIssuesByStatus(Issue.IssueStatus.PENDING),
                "pendingReservations", reservationService.getPendingReservations()
        ));
    }

    // Book CRUD
    @GetMapping("/books")
    public List<Book> listBooks() {
        return bookService.findAll();
    }

    @PostMapping("/books")
    public ResponseEntity<Book> createBook(@Valid @RequestBody BookDto bookDto) {
        Book book = bookService.createBook(bookDto);
        return ResponseEntity.ok(book);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @Valid @RequestBody Book updatedBook) {
        try {
            return ResponseEntity.ok(bookService.updateBook(id, updatedBook));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok("Book deleted");
    }

    // Issue Management
    @GetMapping("/issues/pending")
    public List<Issue> viewPendingIssues() {
        return issueService.getIssuesByStatus(Issue.IssueStatus.PENDING);
    }

    @PutMapping("/issues/approve/{id}")
    public ResponseEntity<String> approveIssue(@PathVariable Long id) {
        issueService.approveIssue(id);
        return ResponseEntity.ok("Issue approved");
    }

    @GetMapping("/issues/return-requests")
    public List<Issue> viewReturnRequests() {
        return issueService.getIssuesByStatus(Issue.IssueStatus.RETURN_REQUESTED);
    }

    @PutMapping("/issues/confirm-return/{id}")
    public ResponseEntity<String> confirmReturn(@PathVariable Long id) {
        issueService.confirmReturn(id);
        return ResponseEntity.ok("Return confirmed");
    }

    // Reservation Management
    @GetMapping("/reservations/pending")
    public List<Reservation> viewPendingReservations() {
        return reservationService.getPendingReservations();
    }

    @PutMapping("/reservations/cancel/{id}")
    public ResponseEntity<String> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.ok("Reservation cancelled");
    }
}
