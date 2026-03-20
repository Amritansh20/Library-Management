package com.libmgmt.borrowing.repository;

import com.libmgmt.borrowing.model.Transaction;
import com.libmgmt.borrowing.model.TransactionStatus;
import com.libmgmt.borrowing.model.TransactionType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends MongoRepository<Transaction, String> {

    List<Transaction> findByMemberId(String memberId);

    List<Transaction> findByBookId(String bookId);

    List<Transaction> findByMemberIdAndStatus(String memberId, TransactionStatus status);

    long countByMemberIdAndStatus(String memberId, TransactionStatus status);

    Optional<Transaction> findByBookItemIdAndStatusAndTransactionType(
            String bookItemId, TransactionStatus status, TransactionType transactionType);

    Optional<Transaction> findByBookItemIdAndStatus(String bookItemId, TransactionStatus status);
}
