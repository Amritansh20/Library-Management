package com.libmgmt.inventory.controller;

import com.libmgmt.inventory.dto.BookItemRequest;
import com.libmgmt.inventory.dto.BookItemResponse;
import com.libmgmt.inventory.service.BookItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookItemController {

    private final BookItemService bookItemService;

    @PostMapping("/api/books/{bookId}/items")
    public ResponseEntity<BookItemResponse> addBookItem(
            @PathVariable String bookId,
            @RequestBody BookItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookItemService.addBookItem(bookId, request));
    }

    @GetMapping("/api/books/{bookId}/items")
    public ResponseEntity<List<BookItemResponse>> getItemsByBookId(@PathVariable String bookId) {
        return ResponseEntity.ok(bookItemService.getItemsByBookId(bookId));
    }

    @GetMapping("/api/book-items/{itemId}")
    public ResponseEntity<BookItemResponse> getItemById(@PathVariable String itemId) {
        return ResponseEntity.ok(bookItemService.getItemById(itemId));
    }

    @PutMapping("/api/book-items/{itemId}")
    public ResponseEntity<BookItemResponse> updateItem(
            @PathVariable String itemId,
            @RequestBody BookItemRequest request) {
        return ResponseEntity.ok(bookItemService.updateItem(itemId, request));
    }

    @DeleteMapping("/api/book-items/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable String itemId) {
        bookItemService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }
}
