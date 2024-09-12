package org.encode.libraryprojectapi.controller;

import lombok.RequiredArgsConstructor;
import org.encode.libraryprojectapi.model.Book;
import org.encode.libraryprojectapi.model.Member;
import org.encode.libraryprojectapi.model.request.BookLendRequest;
import org.encode.libraryprojectapi.model.request.MemberCreationRequest;
import org.encode.libraryprojectapi.service.LibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    private final LibraryService libraryService;
    @GetMapping("/book")
    public ResponseEntity<?> readBooks(@RequestParam(required = false) String isbn) {
        logger.info("Fetching books. ISBN: {}", isbn);
        if (isbn == null) {
            logger.info("ISBN not provided. Fetching all books.");
            return ResponseEntity.ok(libraryService.readBooks());
        }
        logger.debug("ISBN provided. Fetching book with ISBN: {}", isbn);
        return ResponseEntity.ok(libraryService.readBook(isbn));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<Book> readBook(@PathVariable String bookId) {
        logger.info("Fetching book with ID: {}", bookId);
        return ResponseEntity.ok(libraryService.readBookById(bookId));
    }

    @GetMapping("/book/by/author/{authorId}")
    public ResponseEntity<?> readBooksByAuthor(@PathVariable String authorId){
        return ResponseEntity.ok(libraryService.readBooksByAuthor(authorId));
    }

    @PostMapping("/book/borrow")
    public ResponseEntity<List<String>> borrowABook(@RequestBody BookLendRequest bookLendRequests) {
        logger.info("Lending books with request: {}", bookLendRequests);
        return ResponseEntity.ok(libraryService.borrowABook(bookLendRequests));
    }



    @PostMapping("/signup")

    public ResponseEntity<String> createMember(@RequestBody MemberCreationRequest request) {
        logger.info("Creating new member(s) with request: {}", request);
        Member member = new Member();
        member.setFirstName(request.getFirstName());
        member.setLastName(request.getLastName());

        return ResponseEntity.ok(libraryService.signUp(request));
    }
}
