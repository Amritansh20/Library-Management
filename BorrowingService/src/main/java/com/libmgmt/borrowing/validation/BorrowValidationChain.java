package com.libmgmt.borrowing.validation;

import com.libmgmt.borrowing.dto.BorrowRequest;
import com.libmgmt.borrowing.repository.TransactionRepository;
import com.libmgmt.borrowing.strategy.BorrowLimitStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BorrowValidationChain {

    private final TransactionRepository transactionRepository;
    private final BorrowLimitStrategy borrowLimitStrategy;

    public void validate(BorrowRequest request) {
        BorrowValidator chain = new RequestFieldsValidator();
        chain.setNext(new DuplicateBorrowValidator(transactionRepository))
             .setNext(new BorrowLimitValidator(transactionRepository, borrowLimitStrategy));

        chain.validate(request);
    }
}
