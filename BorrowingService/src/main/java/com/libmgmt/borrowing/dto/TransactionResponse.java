package com.libmgmt.borrowing.dto;

import com.libmgmt.borrowing.model.TransactionStatus;
import com.libmgmt.borrowing.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private String id;
    private String bookItemId;
    private String bookId;
    private String memberId;
    private TransactionType transactionType;
    private TransactionStatus status;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private LocalDateTime createdAt;
}
