package com.libmgmt.inventory.state;

import com.libmgmt.inventory.model.BookItemStatus;

public interface BookItemState {

    BookItemStatus getStatus();

    BookItemState borrow();

    BookItemState returnItem();

    BookItemState reserve();

    BookItemState markLost();
}
