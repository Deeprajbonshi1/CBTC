package com.LibraryCatalogSystem.service;
import com.LibraryCatalogSystem.entity.Book;

import java.util.List;

public interface BookService {
    Book createBook(Book book);
    Book getBookById(Long id);
    List<Book> getAllBooks();
    Book updateBook(Long id, Book updatedBook);
    void deleteBook(Long id);
    List<Book> searchByTitle(String titleKeyword);
    List<Book> searchByAuthor(String authorKeyword);
}
