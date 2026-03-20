package com.libmgmt.inventory.service;

import com.libmgmt.inventory.dto.BookRequest;
import com.libmgmt.inventory.dto.BookResponse;
import com.libmgmt.inventory.exception.BookNotFoundException;
import com.libmgmt.inventory.model.Book;
import com.libmgmt.inventory.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public BookResponse createBook(BookRequest request) {
        Book book = Book.builder()
                .isbn(request.getIsbn())
                .title(request.getTitle())
                .author(request.getAuthor())
                .subject(request.getSubject())
                .publicationDate(request.getPublicationDate())
                .totalCopies(0)
                .availableCopies(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return toResponse(bookRepository.save(book));
    }

    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream().map(this::toResponse).toList();
    }

    public BookResponse getBookById(String id) {
        return toResponse(findBookOrThrow(id));
    }

    public List<BookResponse> searchBooks(String title, String author, String isbn, String subject) {
        if (isbn != null && !isbn.isBlank()) {
            return bookRepository.findByIsbn(isbn).map(b -> List.of(toResponse(b))).orElse(List.of());
        }
        if (title != null && !title.isBlank()) {
            return bookRepository.findByTitleContainingIgnoreCase(title).stream().map(this::toResponse).toList();
        }
        if (author != null && !author.isBlank()) {
            return bookRepository.findByAuthorContainingIgnoreCase(author).stream().map(this::toResponse).toList();
        }
        if (subject != null && !subject.isBlank()) {
            return bookRepository.findBySubjectContainingIgnoreCase(subject).stream().map(this::toResponse).toList();
        }
        return getAllBooks();
    }

    public BookResponse updateBook(String id, BookRequest request) {
        Book book = findBookOrThrow(id);
        book.setIsbn(request.getIsbn());
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setSubject(request.getSubject());
        book.setPublicationDate(request.getPublicationDate());
        book.setUpdatedAt(LocalDateTime.now());
        return toResponse(bookRepository.save(book));
    }

    public void deleteBook(String id) {
        findBookOrThrow(id);
        bookRepository.deleteById(id);
    }

    public void incrementAvailableCopies(String bookId) {
        Book book = findBookOrThrow(bookId);
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        book.setUpdatedAt(LocalDateTime.now());
        bookRepository.save(book);
    }

    public void decrementAvailableCopies(String bookId) {
        Book book = findBookOrThrow(bookId);
        if (book.getAvailableCopies() > 0) {
            book.setAvailableCopies(book.getAvailableCopies() - 1);
        }
        book.setUpdatedAt(LocalDateTime.now());
        bookRepository.save(book);
    }

    public void incrementTotalCopies(String bookId) {
        Book book = findBookOrThrow(bookId);
        book.setTotalCopies(book.getTotalCopies() + 1);
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        book.setUpdatedAt(LocalDateTime.now());
        bookRepository.save(book);
    }

    public void decrementTotalCopies(String bookId) {
        Book book = findBookOrThrow(bookId);
        if (book.getTotalCopies() > 0) {
            book.setTotalCopies(book.getTotalCopies() - 1);
        }
        if (book.getAvailableCopies() > 0) {
            book.setAvailableCopies(book.getAvailableCopies() - 1);
        }
        book.setUpdatedAt(LocalDateTime.now());
        bookRepository.save(book);
    }

    private Book findBookOrThrow(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
    }

    private BookResponse toResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .subject(book.getSubject())
                .publicationDate(book.getPublicationDate())
                .totalCopies(book.getTotalCopies())
                .availableCopies(book.getAvailableCopies())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }
}
