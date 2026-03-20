package com.libmgmt.borrowing.service;

import com.libmgmt.borrowing.dto.BorrowRequest;
import com.libmgmt.borrowing.dto.TransactionResponse;
import com.libmgmt.borrowing.event.BorrowingEvent;
import com.libmgmt.borrowing.exception.TransactionNotFoundException;
import com.libmgmt.borrowing.model.Transaction;
import com.libmgmt.borrowing.model.TransactionStatus;
import com.libmgmt.borrowing.model.TransactionType;
import com.libmgmt.borrowing.outbox.OutboxEvent;
import com.libmgmt.borrowing.outbox.OutboxRepository;
import com.libmgmt.borrowing.repository.TransactionRepository;
import com.libmgmt.borrowing.validation.BorrowValidationChain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private static final int BORROW_PERIOD_DAYS = 14;
    private static final String TOPIC = "borrowing-events";

    private final TransactionRepository transactionRepository;
    private final OutboxRepository outboxRepository;
    private final BorrowValidationChain borrowValidationChain;
    private final ObjectMapper objectMapper;

    public TransactionResponse borrowBook(BorrowRequest request) {
        // Chain of Responsibility: validate through the pipeline
        borrowValidationChain.validate(request);

        Transaction transaction = Transaction.builder()
                .bookItemId(request.getBookItemId())
                .bookId(request.getBookId())
                .memberId(request.getMemberId())
                .transactionType(TransactionType.BORROW)
                .status(TransactionStatus.ACTIVE)
                .borrowDate(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusDays(BORROW_PERIOD_DAYS))
                .createdAt(LocalDateTime.now())
                .build();

        Transaction saved = transactionRepository.save(transaction);
        saveOutboxEvent(saved);
        return toResponse(saved);
    }

    public TransactionResponse returnBook(BorrowRequest request) {
        Transaction active = transactionRepository.findByBookItemIdAndStatus(
                        request.getBookItemId(), TransactionStatus.ACTIVE)
                .orElseThrow(() -> new TransactionNotFoundException(
                        "No active borrow transaction found for book item: " + request.getBookItemId()));

        active.setReturnDate(LocalDateTime.now());
        active.setStatus(TransactionStatus.COMPLETED);
        transactionRepository.save(active);

        Transaction returnTransaction = Transaction.builder()
                .bookItemId(request.getBookItemId())
                .bookId(active.getBookId())
                .memberId(active.getMemberId())
                .transactionType(TransactionType.RETURN)
                .status(TransactionStatus.COMPLETED)
                .returnDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        Transaction saved = transactionRepository.save(returnTransaction);
        saveOutboxEvent(saved);
        return toResponse(saved);
    }

    public TransactionResponse reserveBook(BorrowRequest request) {
        Transaction transaction = Transaction.builder()
                .bookItemId(request.getBookItemId())
                .bookId(request.getBookId())
                .memberId(request.getMemberId())
                .transactionType(TransactionType.RESERVE)
                .status(TransactionStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        Transaction saved = transactionRepository.save(transaction);
        saveOutboxEvent(saved);
        return toResponse(saved);
    }

    public TransactionResponse renewBook(String transactionId) {
        Transaction active = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(
                        "Transaction not found with id: " + transactionId));

        if (active.getStatus() != TransactionStatus.ACTIVE ||
                active.getTransactionType() != TransactionType.BORROW) {
            throw new IllegalArgumentException("Only active borrow transactions can be renewed");
        }

        active.setDueDate(active.getDueDate().plusDays(BORROW_PERIOD_DAYS));
        transactionRepository.save(active);

        Transaction renewTransaction = Transaction.builder()
                .bookItemId(active.getBookItemId())
                .bookId(active.getBookId())
                .memberId(active.getMemberId())
                .transactionType(TransactionType.RENEW)
                .status(TransactionStatus.COMPLETED)
                .borrowDate(active.getBorrowDate())
                .dueDate(active.getDueDate())
                .createdAt(LocalDateTime.now())
                .build();

        Transaction saved = transactionRepository.save(renewTransaction);
        saveOutboxEvent(saved);
        return toResponse(saved);
    }

    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll().stream().map(this::toResponse).toList();
    }

    public TransactionResponse getTransactionById(String id) {
        return toResponse(transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + id)));
    }

    public List<TransactionResponse> getTransactionsByMember(String memberId) {
        return transactionRepository.findByMemberId(memberId).stream().map(this::toResponse).toList();
    }

    public List<TransactionResponse> getTransactionsByBook(String bookId) {
        return transactionRepository.findByBookId(bookId).stream().map(this::toResponse).toList();
    }

    /**
     * Outbox Pattern: saves event to MongoDB instead of publishing directly to Kafka.
     * The OutboxPublisher polls and publishes these reliably.
     */
    private void saveOutboxEvent(Transaction transaction) {
        try {
            BorrowingEvent event = BorrowingEvent.builder()
                    .bookItemId(transaction.getBookItemId())
                    .bookId(transaction.getBookId())
                    .memberId(transaction.getMemberId())
                    .transactionType(transaction.getTransactionType().name())
                    .build();

            OutboxEvent outbox = OutboxEvent.builder()
                    .topic(TOPIC)
                    .key(transaction.getBookItemId())
                    .payload(objectMapper.writeValueAsString(event))
                    .published(false)
                    .createdAt(LocalDateTime.now())
                    .build();

            outboxRepository.save(outbox);
        } catch (Exception e) {
            log.error("Failed to save outbox event for transaction {}: {}",
                    transaction.getId(), e.getMessage());
        }
    }

    private TransactionResponse toResponse(Transaction t) {
        return TransactionResponse.builder()
                .id(t.getId())
                .bookItemId(t.getBookItemId())
                .bookId(t.getBookId())
                .memberId(t.getMemberId())
                .transactionType(t.getTransactionType())
                .status(t.getStatus())
                .borrowDate(t.getBorrowDate())
                .dueDate(t.getDueDate())
                .returnDate(t.getReturnDate())
                .createdAt(t.getCreatedAt())
                .build();
    }
}
