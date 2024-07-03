package org.encode.libraryprojectapi.controller;



import org.encode.libraryprojectapi.model.Author;
import org.encode.libraryprojectapi.model.Book;
import org.encode.libraryprojectapi.model.Member;
import org.encode.libraryprojectapi.model.request.AuthorCreationRequest;
import org.encode.libraryprojectapi.model.request.BookCreationRequest;
import org.encode.libraryprojectapi.model.request.BookLendRequest;
import org.encode.libraryprojectapi.model.request.MemberCreationRequest;
import org.encode.libraryprojectapi.service.LibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping(value = "/api/library")
@RequiredArgsConstructor
public class LibraryController {
    private static final Logger logger = LoggerFactory.getLogger(LibraryController.class);
    private final LibraryService libraryService;

    @GetMapping("/book")
    public ResponseEntity readBooks(@RequestParam(required = false) String isbn) {
        logger.info("Fetching books. ISBN: {}", isbn);
        if (isbn == null) {
            logger.info("ISBN not provided. Fetching all books.");
            return ResponseEntity.ok(libraryService.readBooks());
        }
        logger.debug("ISBN provided. Fetching book with ISBN: {}",isbn);
        return ResponseEntity.ok(libraryService.readBook(isbn));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<Book> readBook (@PathVariable String bookId) {
        logger.info("Fetching book with ID: {}", bookId);
        return ResponseEntity.ok(libraryService.readBookById(bookId));
    }

    @PostMapping("/book")
    public ResponseEntity<Book> createBook (@RequestBody BookCreationRequest request) {
        logger.info("Creating new book with request: {}", request);
        return ResponseEntity.ok(libraryService.createBook(request));
    }

    @DeleteMapping("/book/{bookId}")
    public ResponseEntity<Void> deleteBook (@PathVariable String bookId) {
        logger.info("Deleting book with ID: {}", bookId);
        libraryService.deleteBook(bookId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/member")
    public ResponseEntity<String> createMember (@RequestBody List<MemberCreationRequest> request) {
        logger.info("Creating new member(s) with request: {}", request);
        return ResponseEntity.ok(libraryService.createMember(request));
    }

    @PatchMapping("/member/{memberId}")
    public ResponseEntity<Member> updateMember (@RequestBody MemberCreationRequest request, @PathVariable String memberId) {
        logger.info("Updating member with ID: {} and request: {}", memberId, request);
        return ResponseEntity.ok(libraryService.updateMember(memberId, request));
    }

    @PostMapping("/book/lend")
    public ResponseEntity<List<String>> lendABook(@RequestBody BookLendRequest bookLendRequests) {
        logger.info("Lending books with request: {}", bookLendRequests);
        return ResponseEntity.ok(libraryService.lendABook(bookLendRequests));
    }

    @PostMapping("/author")
    public ResponseEntity<Author> createAuthor (@RequestBody AuthorCreationRequest request) {
        logger.info("Creating new author with request: {}", request);
        return ResponseEntity.ok(libraryService.createAuthor(request));
    }

}

