package com.libmgmt.inventory.state;

import com.libmgmt.inventory.model.BookItemStatus;

public class LostState implements BookItemState {

    @Override
    public BookItemStatus getStatus() {
        return BookItemStatus.LOST;
    }

    @Override
    public BookItemState borrow() {
        throw new IllegalStateException("Cannot borrow a lost book item");
    }

    @Override
    public BookItemState returnItem() {
        return new AvailableState();
    }

    @Override
    public BookItemState reserve() {
        throw new IllegalStateException("Cannot reserve a lost book item");
    }

    @Override
    public BookItemState markLost() {
        throw new IllegalStateException("Book item is already marked as lost");
    }
}
