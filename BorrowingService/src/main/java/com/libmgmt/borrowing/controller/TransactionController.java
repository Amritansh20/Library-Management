package com.libmgmt.borrowing.controller;

import com.libmgmt.borrowing.dto.BorrowRequest;
import com.libmgmt.borrowing.dto.TransactionResponse;
import com.libmgmt.borrowing.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/borrow")
    public ResponseEntity<TransactionResponse> borrowBook(@RequestBody BorrowRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.borrowBook(request));
    }

    @PostMapping("/return")
    public ResponseEntity<TransactionResponse> returnBook(@RequestBody BorrowRequest request) {
        return ResponseEntity.ok(transactionService.returnBook(request));
    }

    @PostMapping("/reserve")
    public ResponseEntity<TransactionResponse> reserveBook(@RequestBody BorrowRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.reserveBook(request));
    }

    @PostMapping("/renew/{transactionId}")
    public ResponseEntity<TransactionResponse> renewBook(@PathVariable String transactionId) {
        return ResponseEntity.ok(transactionService.renewBook(transactionId));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable String id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByMember(@PathVariable String memberId) {
        return ResponseEntity.ok(transactionService.getTransactionsByMember(memberId));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByBook(@PathVariable String bookId) {
        return ResponseEntity.ok(transactionService.getTransactionsByBook(bookId));
    }
}
