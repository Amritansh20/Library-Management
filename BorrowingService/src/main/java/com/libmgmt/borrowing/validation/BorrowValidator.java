package com.libmgmt.borrowing.validation;

import com.libmgmt.borrowing.dto.BorrowRequest;

public abstract class BorrowValidator {

    protected BorrowValidator next;

    public BorrowValidator setNext(BorrowValidator next) {
        this.next = next;
        return next;
    }

    public void validate(BorrowRequest request) {
        doValidate(request);
        if (next != null) {
            next.validate(request);
        }
    }

    protected abstract void doValidate(BorrowRequest request);
}
