package com.LibraryCatalogSystem.controller;
import com.LibraryCatalogSystem.entity.Book;
import com.LibraryCatalogSystem.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;


    @PostMapping("/createbook")
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book bookRequest) {
        Book created = bookService.createBook(bookRequest);
        return ResponseEntity
                .created(URI.create("/api/books/" + created.getId()))
                .body(created);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book found = bookService.getBookById(id);
        return ResponseEntity.ok(found);
    }


    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> allBooks = bookService.getAllBooks();
        return ResponseEntity.ok(allBooks);
    }


    @PutMapping("updatebook/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book bookRequest) {
        Book updated = bookService.updateBook(id, bookRequest);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/search/title")
    public ResponseEntity<List<Book>> searchByTitle(@RequestParam("keyword") String titleKeyword) {
        List<Book> results = bookService.searchByTitle(titleKeyword);
        return ResponseEntity.ok(results);
    }


    @GetMapping("/search/author")
    public ResponseEntity<List<Book>> searchByAuthor(
            @RequestParam("keyword") String authorKeyword) {
        List<Book> results = bookService.searchByAuthor(authorKeyword);
        return ResponseEntity.ok(results);
    }
}
