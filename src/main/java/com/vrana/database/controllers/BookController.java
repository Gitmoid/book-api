package com.vrana.database.controllers;

import com.vrana.database.domain.dto.BookDto;
import com.vrana.database.services.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> createBook(
            @PathVariable("isbn") String isbn,
            @Valid @RequestBody BookDto bookDto) {
        return new ResponseEntity<>(bookService.createBook(isbn, bookDto), HttpStatus.CREATED);
    }

    @PutMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> fullUpdateBook(
            @PathVariable("isbn") String isbn,
            @RequestBody BookDto bookDto) {
        return new ResponseEntity<>(bookService.updateFullBook(isbn, bookDto), HttpStatus.OK);
    }

    @PatchMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> partialUpdateBook(
            @PathVariable("isbn") String isbn,
            @RequestBody BookDto bookDto) {
        return new ResponseEntity<>(bookService.updatePartialBook(isbn, bookDto), HttpStatus.OK);
    }

    @GetMapping(path = "/books")
    public Page<BookDto> listBooks(
            Pageable pageable) {
        return bookService.findAllBooks(pageable);
    }

    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> getBook(
            @PathVariable("isbn") String isbn) {
        return new ResponseEntity<>(bookService.findOneOrThrow(isbn), HttpStatus.OK);
    }

    @DeleteMapping(path = "/books/{isbn}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable("isbn") String isbn) {
        bookService.delete(isbn);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
