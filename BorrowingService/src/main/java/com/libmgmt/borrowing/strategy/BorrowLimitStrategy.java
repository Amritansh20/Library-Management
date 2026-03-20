package com.libmgmt.borrowing.strategy;

public interface BorrowLimitStrategy {

    int getMaxBorrowLimit(String memberId);
}
