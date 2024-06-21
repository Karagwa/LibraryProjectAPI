package org.encode.libraryprojectapi.repository;


import org.encode.libraryprojectapi.model.Book;
import org.encode.libraryprojectapi.model.Lend;
import org.encode.libraryprojectapi.model.LendStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LendRepository extends MongoRepository<Lend,String> {
    Optional<Lend> findByBookAndStatus(Book book, LendStatus status);
}
