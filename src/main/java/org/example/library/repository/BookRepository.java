package org.example.library.repository;

import org.example.library.data.BookEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BookRepository extends MongoRepository<BookEntity, String> {
    Optional<BookEntity> findByISBN(String ISBN);
    boolean existsByISBN(String ISBN);
}
