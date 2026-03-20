package com.libmgmt.borrowing.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingEvent {
    private String bookItemId;
    private String bookId;
    private String memberId;
    private String transactionType;
}
