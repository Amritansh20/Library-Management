package com.libmgmt.borrowing.event;

import tools.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BorrowingEventProducer {

    private static final String TOPIC = "borrowing-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void publishEvent(BorrowingEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            log.info("Publishing borrowing event: {}", message);
            kafkaTemplate.send(TOPIC, event.getBookItemId(), message);
        } catch (Exception e) {
            log.error("Error publishing borrowing event: {}", event, e);
        }
    }
}
