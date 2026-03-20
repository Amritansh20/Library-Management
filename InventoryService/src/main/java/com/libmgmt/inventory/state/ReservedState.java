package com.libmgmt.inventory.state;

import com.libmgmt.inventory.model.BookItemStatus;

public class ReservedState implements BookItemState {

    @Override
    public BookItemStatus getStatus() {
        return BookItemStatus.RESERVED;
    }

    @Override
    public BookItemState borrow() {
        return new BorrowedState();
    }

    @Override
    public BookItemState returnItem() {
        throw new IllegalStateException("Cannot return a reserved book that was never borrowed");
    }

    @Override
    public BookItemState reserve() {
        throw new IllegalStateException("Book item is already reserved");
    }

    @Override
    public BookItemState markLost() {
        return new LostState();
    }
}
