package com.libmgmt.inventory.state;

import com.libmgmt.inventory.model.BookItemStatus;

public class AvailableState implements BookItemState {

    @Override
    public BookItemStatus getStatus() {
        return BookItemStatus.AVAILABLE;
    }

    @Override
    public BookItemState borrow() {
        return new BorrowedState();
    }

    @Override
    public BookItemState returnItem() {
        throw new IllegalStateException("Cannot return a book that is not borrowed");
    }

    @Override
    public BookItemState reserve() {
        return new ReservedState();
    }

    @Override
    public BookItemState markLost() {
        return new LostState();
    }
}
