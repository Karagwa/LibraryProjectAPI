package org.encode.libraryprojectapi.repository;


import org.encode.libraryprojectapi.model.Librarian;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LibrarianRepository extends MongoRepository<Librarian, String> {
    Optional<Librarian> findByUsername(String username);
}
