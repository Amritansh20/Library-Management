package com.libmgmt.inventory.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "book_items")
public class BookItem {

    @Id
    private String id;

    private String bookId;

    @Indexed(unique = true)
    private String barcode;

    private String rackNumber;
    private BookItemStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
