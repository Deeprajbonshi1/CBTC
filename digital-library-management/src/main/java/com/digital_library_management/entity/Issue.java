package com.digital_library_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private LocalDate requestDate;

    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private IssueStatus status;

    public enum IssueStatus {
        PENDING,
        ISSUED,
        RETURN_REQUESTED,
        RETURNED
    }
}
