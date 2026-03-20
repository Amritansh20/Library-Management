package com.libmgmt.inventory.event;

import com.libmgmt.inventory.service.BookItemService;
import com.libmgmt.inventory.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryEventConsumer {

    private final BookService bookService;
    private final BookItemService bookItemService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "borrowing-events", groupId = "inventory-service-group")
    public void handleBorrowingEvent(String message) {
        try {
            BorrowingEvent event = objectMapper.readValue(message, BorrowingEvent.class);
            log.info("Received borrowing event: {}", event);

            switch (event.getTransactionType()) {
                case "BORROW" -> {
                    bookItemService.transitionToBorrowed(event.getBookItemId());
                    bookService.decrementAvailableCopies(event.getBookId());
                }
                case "RETURN" -> {
                    bookItemService.transitionToReturned(event.getBookItemId());
                    bookService.incrementAvailableCopies(event.getBookId());
                }
                case "RESERVE" -> {
                    bookItemService.transitionToReserved(event.getBookItemId());
                    bookService.decrementAvailableCopies(event.getBookId());
                }
                case "RENEW" -> log.info("Renew event — no inventory change needed");
                default -> log.warn("Unknown transaction type: {}", event.getTransactionType());
            }
        } catch (IllegalStateException e) {
            log.error("Invalid state transition for event {}: {}", message, e.getMessage());
        } catch (Exception e) {
            log.error("Error processing borrowing event: {}", message, e);
        }
    }
}
