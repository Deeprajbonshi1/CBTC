package com.digital_library_management.service;

import com.digital_library_management.dto.BookDto;
import com.digital_library_management.entity.Book;
import com.digital_library_management.exception.IsbnAlreadyExistsException;
import com.digital_library_management.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.hibernate.ResourceClosedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceClosedException("Book not found with id: " + id));
    }

 /*   @Transactional
    public Book createBook(BookDto bookDto) {
        if (bookRepository.existsByIsbn(bookDto.getIsbn())) {
            throw new RuntimeException("ISBN already exists");
        }

        Book book = Book.builder()
                .title(bookDto.getTitle())
                .author(bookDto.getAuthor())
                .isbn(bookDto.getIsbn())
                .category(bookDto.getCategory())
                .publisher(bookDto.getPublisher())
                .quantityTotal(bookDto.getQuantityTotal())
                .quantityAvailable(bookDto.getQuantityTotal()) // Set available = total initially
                .rackLocation(bookDto.getRackLocation())
                .description(bookDto.getDescription())
                .build();

        return bookRepository.save(book);
    } */

    @Transactional
    public Book createBook(BookDto bookDto) {
        if (bookRepository.existsByIsbn(bookDto.getIsbn())) {
            throw new IsbnAlreadyExistsException("ISBN already exists");
        }

        Book book = Book.builder()
                .title(bookDto.getTitle())
                .author(bookDto.getAuthor())
                .isbn(bookDto.getIsbn())
                .category(bookDto.getCategory())
                .publisher(bookDto.getPublisher())
                .quantityTotal(bookDto.getQuantityTotal())
                .quantityAvailable(bookDto.getQuantityTotal())
                .rackLocation(bookDto.getRackLocation())
                .description(bookDto.getDescription())
                .build();

        try {
            return bookRepository.save(book);
        } catch (DataIntegrityViolationException ex) {
            throw new IsbnAlreadyExistsException("ISBN already exists in the database");
        }
    }

    @Transactional
    public Book updateBook(Long id, Book updated) {
        Book existing = findById(id);
        existing.setTitle(updated.getTitle());
        existing.setAuthor(updated.getAuthor());
        existing.setIsbn(updated.getIsbn());
        existing.setCategory(updated.getCategory());
        existing.setPublisher(updated.getPublisher());

        // If total quantity changed, adjust available accordingly

        int diff = updated.getQuantityTotal() - existing.getQuantityTotal();
        existing.setQuantityTotal(updated.getQuantityTotal());
        existing.setQuantityAvailable(existing.getQuantityAvailable() + diff);
     //   existing.setCoverImagePath(updated.getCoverImagePath());
        existing.setRackLocation(updated.getRackLocation());
        existing.setDescription(updated.getDescription());
        return bookRepository.save(existing);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = findById(id);
        if (!book.getQuantityAvailable().equals(book.getQuantityTotal())) {
            throw new RuntimeException("Cannot delete: some copies are issued or reserved");
        }
        bookRepository.delete(book);
    }

    public List<Book> searchByTitleOrAuthor(String query) {
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query);
    }

    public List<Book> findByCategory(String category) {
        return bookRepository.findByCategory(category);
    }
}
