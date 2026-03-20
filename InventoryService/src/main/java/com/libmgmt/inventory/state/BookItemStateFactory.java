package com.libmgmt.inventory.state;

import com.libmgmt.inventory.model.BookItemStatus;
import org.springframework.stereotype.Component;

@Component
public class BookItemStateFactory {

    public BookItemState fromStatus(BookItemStatus status) {
        return switch (status) {
            case AVAILABLE -> new AvailableState();
            case BORROWED -> new BorrowedState();
            case RESERVED -> new ReservedState();
            case LOST -> new LostState();
        };
    }
}
