package com.libmgmt.inventory.repository;

import com.libmgmt.inventory.model.BookItem;
import com.libmgmt.inventory.model.BookItemStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BookItemRepository extends MongoRepository<BookItem, String> {

    List<BookItem> findByBookId(String bookId);

    Optional<BookItem> findByBarcode(String barcode);

    List<BookItem> findByBookIdAndStatus(String bookId, BookItemStatus status);

    long countByBookId(String bookId);

    long countByBookIdAndStatus(String bookId, BookItemStatus status);
}
