package org.encode.libraryprojectapi.repository;


import org.encode.libraryprojectapi.model.Author;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthorRepository extends MongoRepository<Author, String> {
}

