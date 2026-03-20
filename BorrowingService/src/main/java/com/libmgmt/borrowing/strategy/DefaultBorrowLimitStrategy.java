package com.libmgmt.borrowing.strategy;

import org.springframework.stereotype.Component;

/**
 * Default strategy: all members get a limit of 10.
 * Can be extended with member-type-aware logic when UserService
 * communication is added (e.g., STANDARD=5, PREMIUM=15).
 */
@Component
public class DefaultBorrowLimitStrategy implements BorrowLimitStrategy {

    private static final int STANDARD_LIMIT = 10;

    @Override
    public int getMaxBorrowLimit(String memberId) {
        // Future: call UserService to resolve member type and return
        // different limits for STANDARD vs PREMIUM.
        return STANDARD_LIMIT;
    }
}
