package com.vrana.database.controllers;

import com.vrana.database.domain.dto.ApiErrorResponse;
import com.vrana.database.domain.dto.BookDto;
import com.vrana.database.services.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Books", description = "Operations related to managing books")
public class BookController {

    private final BookService bookService;

    @Operation(summary = "Create a new book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))}),
            @ApiResponse(responseCode = "409", description = "Book already exists",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))})})
    @PostMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> createBook(
            @Parameter(
                    description = "book isbn",
                    required = true)
            @PathVariable("isbn") String isbn,
            @Valid @RequestBody BookDto bookDto) {
        return new ResponseEntity<>(bookService.createBook(isbn, bookDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Create a new book and populate it with information from openlibrary")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book from openlibrary created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))}),
            @ApiResponse(responseCode = "409", description = "Book already exists",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))})})
    @PostMapping(path = "/openbooks/{isbn}")
    public ResponseEntity<BookDto> createOpenBook(
            @Parameter(
                    description = "book isbn to be fetched from openlibrary",
                    required = true)
            @PathVariable("isbn") String isbn) {
        return new ResponseEntity<>(bookService.createOpenBook(isbn), HttpStatus.CREATED);
    }

    @Operation(summary = "Completely update a book",
            description = "Updates all fields of an existing book. Fields that are not provided are set to null.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))})})
    @PutMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> fullUpdateBook(
            @Parameter(
                    description = "book isbn",
                    required = true)
            @PathVariable("isbn") String isbn,
            @RequestBody BookDto bookDto) {
        return new ResponseEntity<>(bookService.updateFullBook(isbn, bookDto), HttpStatus.OK);
    }

    @Operation(summary = "Partially update a book",
            description = "Updates only the fields that are present in the request body.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))})})
    @PatchMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> partialUpdateBook(
            @Parameter(
                    description = "book isbn",
                    required = true)
            @PathVariable("isbn") String isbn,
            @RequestBody BookDto bookDto) {
        return new ResponseEntity<>(bookService.updatePartialBook(isbn, bookDto), HttpStatus.OK);
    }

    @Operation(summary = "Get a paginated list of all books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of books",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class, subTypes = {BookDto.class}))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))})})
    @GetMapping(path = "/books")
    public Page<BookDto> listBooks(
            @Parameter(
                    description = "Pagination parameters")
            Pageable pageable) {
        return bookService.findAllBooks(pageable);
    }

    @Operation(summary = "Get a book by its isbn")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDto.class))}),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "405", description = "Method not supported",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))})})
    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> getBook(
            @Parameter(
                    description = "book isbn",
                    required = true)
            @PathVariable("isbn") String isbn) {
        return new ResponseEntity<>(bookService.findOneOrThrow(isbn), HttpStatus.OK);
    }

    @Operation(summary = "Delete a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))})})
    @DeleteMapping(path = "/books/{isbn}")
    public ResponseEntity<HttpStatus> deleteBook(
            @Parameter(
                    description = "book isbn",
                    required = true)
            @PathVariable("isbn") String isbn) {
        bookService.delete(isbn);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
