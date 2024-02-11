package org.example.authentication.repository;

import org.example.authentication.data.BookDao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface BookRepository extends MongoRepository<BookDao, String> {
}
