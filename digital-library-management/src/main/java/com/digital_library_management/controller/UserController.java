package com.digital_library_management.controller;

import com.digital_library_management.entity.*;
import com.digital_library_management.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private IssueService issueService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private FineService fineService;

    @GetMapping("/books")
    public List<Book> browseBooks(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category) {

        if (search != null && !search.isEmpty()) {
            return bookService.searchByTitleOrAuthor(search);
        } else if (category != null && !category.isEmpty()) {
            return bookService.findByCategory(category);
        } else {
            return bookService.findAll();
        }
    }

    @GetMapping("/books/{id}")
    public Book viewBookDetails(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PostMapping("/issue/{bookId}")
    public ResponseEntity<String> requestIssue(@PathVariable Long bookId, Authentication auth) {
        User user = userService.findByEmail(auth.getName()); // ✅ getName() now works
        issueService.requestIssue(user.getId(), bookId);
        return ResponseEntity.ok("Issue requested");
    }

    @GetMapping("/my-issues")
    public List<Issue> myIssues(Authentication auth) {
        System.out.println("Authenticated email: " + auth.getName());
        User user = userService.findByEmail(auth.getName());
        return issueService.getUserIssues(user.getId(), Issue.IssueStatus.ISSUED);
    }

    @PostMapping("/request-return/{issueId}")
    public ResponseEntity<String> requestReturn(@PathVariable Long issueId) {
        issueService.requestReturn(issueId);
        return ResponseEntity.ok("Return requested");
    }

    @GetMapping("/my-reservations")
    public List<Reservation> myReservations(Authentication auth) {
        User user = userService.findByEmail(auth.getName());
        return reservationService.getUserReservations(user.getId());
    }

    @PostMapping("/reserve/{bookId}")
    public ResponseEntity<String> reserveBook(@PathVariable Long bookId, Authentication auth) {
        User user = userService.findByEmail(auth.getName());
        reservationService.createReservation(user.getId(), bookId);
        return ResponseEntity.ok("Book reserved");
    }

    @GetMapping("/my-fines")
    public List<Fine> myFines(Authentication auth) {
        User user = userService.findByEmail(auth.getName());
        return fineService.getUserFines(user.getId());
    }

    @PutMapping("/pay-fine/{fineId}")
    public ResponseEntity<String> payFine(@PathVariable Long fineId) {
        fineService.payFine(fineId);
        return ResponseEntity.ok("Fine paid");
    }

    @GetMapping("/profile")
    public User profile(Authentication auth) {
        return userService.findByEmail(auth.getName());
    }
}
