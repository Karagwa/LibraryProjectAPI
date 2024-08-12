package org.encode.libraryprojectapi.repository;

import org.bson.types.ObjectId;
import org.encode.libraryprojectapi.model.Book;
import org.encode.libraryprojectapi.model.LendStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book,String> {
    Optional<Book> findByIsbn(String isbn);
    List<Book> findByStatus(LendStatus status);
    //List<Book> findByAuthorId(String authorId);

    @Query("{'lendStatus': {$eq: ?0}}")
    List<Book> getBorrowedBooks(LendStatus status);

    @Query("{'lendStatus': {$eq: ?0}}")
    List<Book> getAvailableBooks(LendStatus status);

    //@Query(fields = "{'author.$ref': 1}")
    List<Book> findAll();

    List<Book>findBooksByAuthorId(ObjectId authorId);

    @Query(value = "{'_id': ?0}", delete = true)
    void deleteBookById(String id);

}
