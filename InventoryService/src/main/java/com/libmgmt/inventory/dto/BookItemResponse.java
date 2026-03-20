package com.libmgmt.inventory.dto;

import com.libmgmt.inventory.model.BookItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookItemResponse {
    private String id;
    private String bookId;
    private String barcode;
    private String rackNumber;
    private BookItemStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
