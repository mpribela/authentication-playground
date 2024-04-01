package org.example.authentication.repository;

import org.example.authentication.data.BookEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BookRepository extends MongoRepository<BookEntity, String> {
    Optional<BookEntity> findByISBN(String ISBN);
    boolean existsByISBN(String ISBN);
}
