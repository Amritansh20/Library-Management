package com.libmgmt.borrowing.validation;

import com.libmgmt.borrowing.dto.BorrowRequest;
import com.libmgmt.borrowing.exception.BorrowLimitExceededException;
import com.libmgmt.borrowing.model.TransactionStatus;
import com.libmgmt.borrowing.repository.TransactionRepository;
import com.libmgmt.borrowing.strategy.BorrowLimitStrategy;

public class BorrowLimitValidator extends BorrowValidator {

    private final TransactionRepository transactionRepository;
    private final BorrowLimitStrategy limitStrategy;

    public BorrowLimitValidator(TransactionRepository transactionRepository,
                                BorrowLimitStrategy limitStrategy) {
        this.transactionRepository = transactionRepository;
        this.limitStrategy = limitStrategy;
    }

    @Override
    protected void doValidate(BorrowRequest request) {
        long activeCount = transactionRepository.countByMemberIdAndStatus(
                request.getMemberId(), TransactionStatus.ACTIVE);
        int maxLimit = limitStrategy.getMaxBorrowLimit(request.getMemberId());
        if (activeCount >= maxLimit) {
            throw new BorrowLimitExceededException(
                    "Member has reached the maximum borrow limit of " + maxLimit + " books");
        }
    }
}
