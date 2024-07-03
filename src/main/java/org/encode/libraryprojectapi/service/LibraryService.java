package org.encode.libraryprojectapi.service;


import org.encode.libraryprojectapi.model.request.AuthorCreationRequest;
import org.encode.libraryprojectapi.exception.EntityNotFoundException;
import org.encode.libraryprojectapi.model.*;
import org.encode.libraryprojectapi.model.request.BookCreationRequest;
import org.encode.libraryprojectapi.model.request.BookLendRequest;
import org.encode.libraryprojectapi.model.request.MemberCreationRequest;
import org.encode.libraryprojectapi.repository.AuthorRepository;
import org.encode.libraryprojectapi.repository.BookRepository;
import org.encode.libraryprojectapi.repository.LendRepository;
import org.encode.libraryprojectapi.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LibraryService {
    private static final Logger logger= LoggerFactory.getLogger(LibraryService.class);

    private final AuthorRepository authorRepository;
    private final MemberRepository memberRepository;
    private final LendRepository lendRepository;
    private final BookRepository bookRepository;

    public Book readBookById(String id) {
        Optional<Book> book = bookRepository.findById(id);
        logger.debug("Reading book with id {}", id);
        if (book.isPresent()) {
            return book.get();
        }
        throw new EntityNotFoundException("Cant find any book under given ID");
    }

    public List<Book> readBooks() {
        logger.debug("Reading all the books in the library");
        return bookRepository.findAll();
    }

    public Book readBook(String isbn) {
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        logger.debug("Reading book with ISBN{}", isbn);
        if (book.isPresent()) {
            return book.get();
        }
        throw new EntityNotFoundException("Cant find any book under given ISBN");
    }

    public Book createBook(BookCreationRequest book) {
        logger.debug("Creating a book record");
        Optional<Author> author = authorRepository.findById(book.getAuthorId());
        if (!author.isPresent()) {
            throw new EntityNotFoundException("Author Not Found");
        }
        Book bookToCreate = new Book();
        BeanUtils.copyProperties(book, bookToCreate);
        bookToCreate.setAuthor(author.get());
        return bookRepository.save(bookToCreate);
    }

    public void deleteBook(String id) {
        logger.debug("Deleting book with ID: {}", id);
        bookRepository.deleteById(id);
    }

    public String createMember(List<MemberCreationRequest> request) {
        logger.debug("Creating members with request: {}", request);

        request.forEach(r->{
            Member member = new Member();
            BeanUtils.copyProperties(r, member);
            member.setStatus(MemberStatus.ACTIVE);
            memberRepository.save(member);
        });

        return "Successfully Added";
    }

    public Member updateMember (String id, MemberCreationRequest request) {
        logger.debug("Updating member with ID: {} and request{}", id, request);
        Optional<Member> optionalMember = memberRepository.findById(id);
        if (!optionalMember.isPresent()) {
            throw new EntityNotFoundException("Member not present in the database");
        }
        Member member = optionalMember.get();
        member.setLastName(request.getLastName());
        member.setFirstName(request.getFirstName());
        return memberRepository.save(member);
    }

    public Author createAuthor (AuthorCreationRequest request) {
        logger.debug("Creating an author with request{}", request);
        Author author = new Author();
        BeanUtils.copyProperties(request, author);
        return authorRepository.save(author);
    }

    public List<String> lendABook (BookLendRequest request) {
        logger.debug("Lending a book with request{}" ,request);
        Optional<Member> memberForId = memberRepository.findById(request.getMemberId());
        if (!memberForId.isPresent()) {
            throw new EntityNotFoundException("Member not present in the database");
        }

        Member member = memberForId.get();
        if (member.getStatus() != MemberStatus.ACTIVE) {
            throw new RuntimeException("User is not active to proceed a lending.");
        }

        List<String> booksApprovedToBurrow = new ArrayList<>();

        request.getBookIds().forEach(bookId -> {

            Optional<Book> bookForId = bookRepository.findById(bookId);
            if (!bookForId.isPresent()) {
                throw new EntityNotFoundException("Cant find any book under given ID");
            }

            Optional<Lend> burrowedBook = lendRepository.findByBookAndStatus(bookForId.get(), LendStatus.BURROWED);
            if (!burrowedBook.isPresent()) {
                booksApprovedToBurrow.add(bookForId.get().getName());
                Lend lend = new Lend();
                lend.setMember(memberForId.get());
                lend.setBook(bookForId.get());
                lend.setStatus(LendStatus.BURROWED);
                lend.setStartOn(Instant.now());
                lend.setDueOn(Instant.now().plus(30, ChronoUnit.DAYS));
                lendRepository.save(lend);
            }

        });
        return booksApprovedToBurrow;
    }
}
