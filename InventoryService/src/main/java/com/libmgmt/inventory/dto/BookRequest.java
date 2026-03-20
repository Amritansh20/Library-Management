package com.libmgmt.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {
    private String isbn;
    private String title;
    private String author;
    private String subject;
    private LocalDate publicationDate;
}
