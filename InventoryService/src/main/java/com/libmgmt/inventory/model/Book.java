package com.libmgmt.inventory.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "books")
public class Book {

    @Id
    private String id;

    @Indexed(unique = true)
    private String isbn;

    private String title;
    private String author;
    private String subject;
    private LocalDate publicationDate;
    private int totalCopies;
    private int availableCopies;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
