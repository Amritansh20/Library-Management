package com.libmgmt.borrowing.validation;

import com.libmgmt.borrowing.dto.BorrowRequest;
import com.libmgmt.borrowing.model.TransactionStatus;
import com.libmgmt.borrowing.repository.TransactionRepository;

public class DuplicateBorrowValidator extends BorrowValidator {

    private final TransactionRepository transactionRepository;

    public DuplicateBorrowValidator(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    protected void doValidate(BorrowRequest request) {
        boolean alreadyBorrowed = transactionRepository
                .findByBookItemIdAndStatus(request.getBookItemId(), TransactionStatus.ACTIVE)
                .isPresent();
        if (alreadyBorrowed) {
            throw new IllegalArgumentException(
                    "Book item " + request.getBookItemId() + " is already borrowed");
        }
    }
}
