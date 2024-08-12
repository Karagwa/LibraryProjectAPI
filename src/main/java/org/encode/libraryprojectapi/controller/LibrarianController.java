package org.encode.libraryprojectapi.controller;


import lombok.RequiredArgsConstructor;
import org.encode.libraryprojectapi.model.*;
import org.encode.libraryprojectapi.model.request.AuthorCreationRequest;
import org.encode.libraryprojectapi.model.request.BookCreationRequest;
import org.encode.libraryprojectapi.model.request.MemberCreationRequest;
import org.encode.libraryprojectapi.repository.LibrarianRepository;
import org.encode.libraryprojectapi.service.LibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/librarian")
@RequiredArgsConstructor
public class LibrarianController {
    private static final Logger logger = LoggerFactory.getLogger(LibrarianController.class);
    private final LibraryService libraryService;
    private final LibrarianRepository librarianRepository;
    @GetMapping("/book")
    public ResponseEntity<?> readBooks(@RequestParam(required = false) String id) {
        logger.info("Fetching books. ID: {}", id);
        if (id == null) {
            logger.info("ID not provided. Fetching all books.");
            return ResponseEntity.ok(libraryService.readBooks());
        }
        logger.debug("ISBN provided. Fetching book with ID: {}", id);
        return ResponseEntity.ok(libraryService.readBook(id));
    }

    @GetMapping("/book/by/author/{authorId}")
    public ResponseEntity<?> readBooksByAuthor(@PathVariable String authorId){
        return ResponseEntity.ok(libraryService.readBooksByAuthor(authorId));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<Book> readBook(@PathVariable String bookId) {
        logger.info("Fetching book with ID: {}", bookId);
        return ResponseEntity.ok(libraryService.readBookById(bookId));
    }

    @PostMapping("/book")
    public ResponseEntity<String> createBook(@RequestBody List<BookCreationRequest> request) {
        logger.info("Creating new book with request: {}", request);
        return ResponseEntity.ok(libraryService.createBook(request));
    }

    @DeleteMapping("/book/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable String bookId) {
        logger.info("Deleting book with ID: {}", bookId);
        libraryService.deleteBookById(bookId);
        return ResponseEntity.ok().build();
    }

//    @GetMapping("/book/author")
//    public ResponseEntity<List<Book>> getBookByAuthor(@RequestParam String authorId) {
//        logger.info("Getting books by authorId {}", authorId);
//        return ResponseEntity.ok(libraryService.findBooksByAuthorId(authorId));
//    }

    // Member mappings
    @PostMapping("/member")
    public ResponseEntity<String> createMember(@RequestBody List<MemberCreationRequest> request) {
        logger.info("Creating new member(s) with request: {}", request);
        return ResponseEntity.ok(libraryService.createMember(request));
    }

    @GetMapping("/member")
    public ResponseEntity<?> readMembers() {
        logger.info("Reading all members");
        return ResponseEntity.ok(libraryService.readMembers());
    }

    @PatchMapping("/member/{memberId}")
    public ResponseEntity<Member> updateMember(@RequestBody MemberCreationRequest request, @PathVariable String memberId) {
        logger.info("Updating member with ID: {} and request: {}", memberId, request);
        return ResponseEntity.ok(libraryService.updateMember(memberId, request));
    }

    @DeleteMapping("/member/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        logger.info("Deleting member with ID: {}", id);
        libraryService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/member/active")
    public ResponseEntity<List<Member>> getActiveMembers() {
        logger.info("Fetching activated members");
        return ResponseEntity.ok(libraryService.findByMemberStatus(MemberStatus.ACTIVE));
    }

    @GetMapping("/member/deactivated")
    public ResponseEntity<List<Member>> getDeactivatedMembers() {
        logger.info("Fetching deactivated members");
        return ResponseEntity.ok(libraryService.findByMemberStatus(MemberStatus.DEACTIVATED));
    }
    @GetMapping("/book/available")
    public ResponseEntity<List<Book>> getAvailableBooks() {
        logger.info("Fetching books by status");
        return ResponseEntity.ok(libraryService.findByLendStatus(LendStatus.AVAILABLE));
    }





    // Author mappings
    @PostMapping("/author")
    public ResponseEntity<String> createAuthor(@RequestBody List<AuthorCreationRequest> request) {
        logger.info("Creating new author with request: {}", request);
        return ResponseEntity.ok(libraryService.createAuthor(request));
    }

    @GetMapping("/author")
    public ResponseEntity<?> readAuthors() {
        logger.info("Fetching all authors.");
        return ResponseEntity.ok(libraryService.readAuthors());
    }

//    @DeleteMapping("/delete/author/{authorId}")
//    public ResponseEntity<Void> deleteAuthor(@PathVariable String authorId) {
//        logger.info("Deleting author with id: {}", authorId);
//        libraryService.deleteBook(authorId);
//        return ResponseEntity.ok().build();
//    }
    @GetMapping("/librarian/{id}")
    public Librarian getLibrarian(@PathVariable String id) {
        return librarianRepository.findById(id).orElseThrow(() -> new RuntimeException("Librarian not found"));
    }

    @PatchMapping("/deactivateMember/{memberId}")
    public ResponseEntity<Member> deactivateMember(@PathVariable String memberId){
        Member deactivateMember = libraryService.deactivateMember(memberId);
        return ResponseEntity.ok(deactivateMember);
    }

}
