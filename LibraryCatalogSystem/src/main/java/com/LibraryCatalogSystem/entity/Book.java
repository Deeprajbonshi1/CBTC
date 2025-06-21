package com.LibraryCatalogSystem.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Author is mandatory")
    private String author;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "ISBN is mandatory")
    private String isbn;

    private String category;

    @NotNull(message = "Year is mandatory")
    private Integer year;
}
