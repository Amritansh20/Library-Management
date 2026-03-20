package com.libmgmt.borrowing.outbox;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "outbox_events")
public class OutboxEvent {

    @Id
    private String id;

    private String topic;
    private String key;
    private String payload;
    private boolean published;

    private LocalDateTime createdAt;
    private LocalDateTime publishedAt;
}
