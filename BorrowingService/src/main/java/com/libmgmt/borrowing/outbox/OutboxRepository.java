package com.libmgmt.borrowing.outbox;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OutboxRepository extends MongoRepository<OutboxEvent, String> {

    List<OutboxEvent> findByPublishedFalseOrderByCreatedAtAsc();
}
