package com.libmgmt.borrowing.validation;

import com.libmgmt.borrowing.dto.BorrowRequest;

public class RequestFieldsValidator extends BorrowValidator {

    @Override
    protected void doValidate(BorrowRequest request) {
        if (request.getMemberId() == null || request.getMemberId().isBlank()) {
            throw new IllegalArgumentException("Member ID is required");
        }
        if (request.getBookItemId() == null || request.getBookItemId().isBlank()) {
            throw new IllegalArgumentException("Book Item ID is required");
        }
        if (request.getBookId() == null || request.getBookId().isBlank()) {
            throw new IllegalArgumentException("Book ID is required");
        }
    }
}
