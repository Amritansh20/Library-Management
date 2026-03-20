package com.libmgmt.inventory.state;

import com.libmgmt.inventory.model.BookItemStatus;

public class BorrowedState implements BookItemState {

    @Override
    public BookItemStatus getStatus() {
        return BookItemStatus.BORROWED;
    }

    @Override
    public BookItemState borrow() {
        throw new IllegalStateException("Book item is already borrowed");
    }

    @Override
    public BookItemState returnItem() {
        return new AvailableState();
    }

    @Override
    public BookItemState reserve() {
        throw new IllegalStateException("Cannot reserve a book item that is already borrowed");
    }

    @Override
    public BookItemState markLost() {
        return new LostState();
    }
}
