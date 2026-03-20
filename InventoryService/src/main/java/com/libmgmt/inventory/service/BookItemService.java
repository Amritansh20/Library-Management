package com.libmgmt.inventory.service;

import com.libmgmt.inventory.dto.BookItemRequest;
import com.libmgmt.inventory.dto.BookItemResponse;
import com.libmgmt.inventory.exception.BookNotFoundException;
import com.libmgmt.inventory.model.BookItem;
import com.libmgmt.inventory.model.BookItemStatus;
import com.libmgmt.inventory.repository.BookItemRepository;
import com.libmgmt.inventory.state.BookItemState;
import com.libmgmt.inventory.state.BookItemStateFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookItemService {

    private final BookItemRepository bookItemRepository;
    private final BookService bookService;
    private final BookItemStateFactory stateFactory;

    public BookItemResponse addBookItem(String bookId, BookItemRequest request) {
        bookService.getBookById(bookId);

        BookItem item = BookItem.builder()
                .bookId(bookId)
                .barcode(request.getBarcode())
                .rackNumber(request.getRackNumber())
                .status(BookItemStatus.AVAILABLE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        BookItem saved = bookItemRepository.save(item);
        bookService.incrementTotalCopies(bookId);
        return toResponse(saved);
    }

    public List<BookItemResponse> getItemsByBookId(String bookId) {
        return bookItemRepository.findByBookId(bookId).stream().map(this::toResponse).toList();
    }

    public BookItemResponse getItemById(String itemId) {
        return toResponse(findItemOrThrow(itemId));
    }

    public BookItemResponse updateItem(String itemId, BookItemRequest request) {
        BookItem item = findItemOrThrow(itemId);
        item.setBarcode(request.getBarcode());
        item.setRackNumber(request.getRackNumber());
        item.setUpdatedAt(LocalDateTime.now());
        return toResponse(bookItemRepository.save(item));
    }

    public void deleteItem(String itemId) {
        BookItem item = findItemOrThrow(itemId);
        bookItemRepository.deleteById(itemId);
        bookService.decrementTotalCopies(item.getBookId());
    }

    // -- State Pattern transition methods --

    public void transitionToBorrowed(String itemId) {
        BookItem item = findItemOrThrow(itemId);
        BookItemState currentState = stateFactory.fromStatus(item.getStatus());
        BookItemState newState = currentState.borrow();
        item.setStatus(newState.getStatus());
        item.setUpdatedAt(LocalDateTime.now());
        bookItemRepository.save(item);
    }

    public void transitionToReturned(String itemId) {
        BookItem item = findItemOrThrow(itemId);
        BookItemState currentState = stateFactory.fromStatus(item.getStatus());
        BookItemState newState = currentState.returnItem();
        item.setStatus(newState.getStatus());
        item.setUpdatedAt(LocalDateTime.now());
        bookItemRepository.save(item);
    }

    public void transitionToReserved(String itemId) {
        BookItem item = findItemOrThrow(itemId);
        BookItemState currentState = stateFactory.fromStatus(item.getStatus());
        BookItemState newState = currentState.reserve();
        item.setStatus(newState.getStatus());
        item.setUpdatedAt(LocalDateTime.now());
        bookItemRepository.save(item);
    }

    public void transitionToLost(String itemId) {
        BookItem item = findItemOrThrow(itemId);
        BookItemState currentState = stateFactory.fromStatus(item.getStatus());
        BookItemState newState = currentState.markLost();
        item.setStatus(newState.getStatus());
        item.setUpdatedAt(LocalDateTime.now());
        bookItemRepository.save(item);
    }

    private BookItem findItemOrThrow(String itemId) {
        return bookItemRepository.findById(itemId)
                .orElseThrow(() -> new BookNotFoundException("BookItem not found with id: " + itemId));
    }

    private BookItemResponse toResponse(BookItem item) {
        return BookItemResponse.builder()
                .id(item.getId())
                .bookId(item.getBookId())
                .barcode(item.getBarcode())
                .rackNumber(item.getRackNumber())
                .status(item.getStatus())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }
}
