package com.LibraryCatalogSystem.service;
import com.LibraryCatalogSystem.entity.Book;
import com.LibraryCatalogSystem.exception.BookNotFoundException;
import com.LibraryCatalogSystem.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book updateBook(Long id, Book updatedBook) {
        Book existing = getBookById(id);
        existing.setTitle(updatedBook.getTitle());
        existing.setAuthor(updatedBook.getAuthor());
        existing.setIsbn(updatedBook.getIsbn());
        existing.setCategory(updatedBook.getCategory());
        existing.setYear(updatedBook.getYear());
        return bookRepository.save(existing);
    }

    @Override
    public void deleteBook(Long id) {
        Book existing = getBookById(id);
        bookRepository.delete(existing);
    }

    @Override
    public List<Book> searchByTitle(String titleKeyword) {
        return bookRepository.findByTitleContainingIgnoreCase(titleKeyword);
    }

    @Override
    public List<Book> searchByAuthor(String authorKeyword) {
        return bookRepository.findByAuthorContainingIgnoreCase(authorKeyword);
    }
}
