package org.encode.libraryprojectapi.service;


import org.bson.types.ObjectId;
import org.encode.libraryprojectapi.model.Response.BookResponse;
import org.encode.libraryprojectapi.model.request.*;
import org.encode.libraryprojectapi.exception.EntityNotFoundException;
import org.encode.libraryprojectapi.model.*;
import org.encode.libraryprojectapi.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LibraryService {
    private static final Logger logger= LoggerFactory.getLogger(LibraryService.class);

    private final AuthorRepository authorRepository;
    private final MemberRepository memberRepository;
    private final LibrarianRepository librarianRepository;

    private final LendRepository lendRepository;
    private final BookRepository bookRepository;
    private final MongoTemplate mongoTemplate;
    private final PasswordEncoder passwordEncoder;


    //book logic
    public Book readBookById(String id) {
        Optional<Book> book = bookRepository.findById(id);
        logger.debug("Reading book with id {}", id);
        if (book.isPresent()) {
            return book.get();
        }
        throw new EntityNotFoundException("Cant find any book under given ID");
    }

    public List<Book> readBooksByAuthor(String id){
        logger.debug("Reading all the books in the library by authorId, {}",id);
        ObjectId authorIdObj = new ObjectId(id);
        return  bookRepository.findBooksByAuthorId(authorIdObj);
    }
    public List<Book> readBooks() {
        logger.debug("Reading all the books in the library");

        return bookRepository.findAll();
    }

    public Book readBook(String id) {
        Optional<Book> book = bookRepository.findById(id);
        logger.debug("Reading book with ISBN{}", id);
        if (book.isPresent()) {
            return book.get();
        }
        throw new EntityNotFoundException("Cant find any book under given ISBN");
    }

    public String createBook(List<BookCreationRequest> books) {
        logger.debug("Creating a book record");

        books.forEach(book-> {

            Book bookToCreate = new Book();
            bookToCreate.setTitle(book.getTitle());
            bookToCreate.setIsbn(book.getIsbn());
            logger.info("Setting the writer");
            bookToCreate.setAuthorId(book.getAuthorId());
            bookToCreate.setStatus(LendStatus.AVAILABLE);
            bookRepository.save(bookToCreate);
        });

        return "Successfully added";
    }
//
//    public List<BookResponse> getBooksByIds(List<String> bookIds) {
//        // Fetch books
//        List<Book> books = bookRepository.findAllById(bookIds);
//
//        if (books.isEmpty()) {
//            throw new ResourceNotFoundException("Books not found");
//        }
//
//        // Map to BookResponse with author details
//        List<BookResponse> bookResponses = books.stream().map(book -> {
//            Author author = authorRepository.findById(book.getAuthorId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Author not found for book: " + book.getTitle()));
//            return new BookResponse(book, author);
//        }).collect(Collectors.toList());
//
//        return bookResponses;
//    }

    //member logic
    public String createMember(List<MemberCreationRequest> requests) {
        logger.debug("Creating members with request: {}", requests);

        requests.forEach(request->{
            Member member = new Member();
            member.setUsername(request.getUsername());
            member.setPassword(passwordEncoder.encode(request.getPassword()));
            member.setFirstName(request.getFirstName());
            member.setLastName(request.getLastName());
            member.setRole(Role.ROLE_MEMBER);
            member.setStatus(MemberStatus.ACTIVE);
            memberRepository.save(member);
            mongoTemplate.save(member, "users");
        });

        return "Successfully Added";
    }

    public String signUp(MemberCreationRequest request) {
        logger.debug("Creating members with request: {}", request);


            Member member = new Member();
            member.setUsername(request.getUsername());
            member.setPassword(passwordEncoder.encode(request.getPassword()));
            member.setFirstName(request.getFirstName());
            member.setLastName(request.getLastName());

            member.setRole(Role.ROLE_MEMBER);
            member.setStatus(MemberStatus.ACTIVE);
            memberRepository.save(member);
            mongoTemplate.save(member, "users");


        return "Successfully Added";
    }

    public String signUp(LibrarianCreationRequest request) {
        logger.debug("Creating librarian with request: {}", request);


        Librarian librarian = new Librarian();
        librarian.setUsername(request.getUsername());
        librarian.setPassword(passwordEncoder.encode(request.getPassword()));
        librarian.setFirstName(request.getFirstName());
        librarian.setLastName(request.getLastName());

        librarian.setRole(Role.ROLE_LIBRARIAN);

        librarianRepository.save(librarian);
        mongoTemplate.save(librarian, "users");


        return "Successfully Added";
    }



    public Member updateMember (String id, MemberCreationRequest request) {
        logger.debug("Updating member with ID: {} and request{}", id, request);
        Optional<Member> optionalMember = memberRepository.findById(id);
        if (optionalMember.isEmpty()) {
            throw new EntityNotFoundException("Member not present in the database");
        }
        Member member = optionalMember.get();
        member.setFirstName(request.getFirstName());
        member.setLastName(request.getLastName());
        return memberRepository.save(member);
    }

    public List<Member> readMembers(){
        logger.debug("Reading all books");
        return memberRepository.findAll();
    }

    public void deleteById(String id){
        logger.debug("Deleting member with ID: {}", id);
        Optional<Member> member= memberRepository.findById(id);
        memberRepository.deleteById(id);
    }

    public Member deactivateMember(String memberId){
        Member member= memberRepository.findById(memberId).orElseThrow();
        member.setStatus(MemberStatus.DEACTIVATED);
        return memberRepository.save(member);
    }

    //authors
    public String createAuthor (List<AuthorCreationRequest> request) {
        logger.debug("Creating an author with request{}", request);

        request.forEach(r->{
            Author author = new Author();
            author.setFirstName(r.getFirstName());
            author.setLastName(r.getLastName());

            authorRepository.save(author);
        });

        return "Successfully added";
    }

    public List<Author> readAuthors() {
        logger.debug("Reading all the authors in the library");
        return authorRepository.findAll();
    }

    public void deleteBookById(String id){
        Optional<Book> optionalBook = bookRepository.findById(id);
        if(optionalBook.isEmpty()){
            throw new EntityNotFoundException("Book not present in the database");
        }
        bookRepository.deleteBookById(id);
    }

    //lend a book
    public List<String> borrowABook (BookLendRequest request) {
        logger.debug("Lending a book with request{}" ,request);
        Optional<Member> memberForId = memberRepository.findById(request.getMemberId());
        if (memberForId.isEmpty()) {
            throw new EntityNotFoundException("Member not present in the database");
        }

        Member member = memberForId.get();
        if (member.getStatus() != MemberStatus.ACTIVE) {
            throw new RuntimeException("User is not active to proceed a lending.");
        }

        List<String> booksApprovedToBurrow = new ArrayList<>();

        request.getBookIds().forEach(bookId -> {

            Optional<Book> bookForId = bookRepository.findById(bookId);
            if (bookForId.isEmpty()) {
                throw new EntityNotFoundException("Cant find any book under given ID");
            }

            Optional<Lend> burrowedBook = lendRepository.findByBookAndStatus(bookForId.get(), LendStatus.BORROWED);
            if (burrowedBook.isEmpty()) {
                booksApprovedToBurrow.add(bookForId.get().getTitle());
                Lend lend = new Lend();
                lend.setMember(memberForId.get());
                lend.setBook(bookForId.get());
                lend.setStatus(LendStatus.BORROWED);
                lend.setStartOn(Instant.now());
                lend.setDueOn(Instant.now().plus(30, ChronoUnit.DAYS));
                lendRepository.save(lend);
            }

        });
        return booksApprovedToBurrow;
    }

    public List<Book> findByLendStatus(LendStatus status){
        return bookRepository.findByStatus(status);

    }

    public List<Member> findByMemberStatus(MemberStatus status){
        return memberRepository.findMembersByStatus(status);
    }



//    public List<Book> findBooksByAuthorId(String authorId){
//
//
//        return bookRepository.findByAuthorId(authorId);
//    }


    }
