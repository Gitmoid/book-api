package com.vrana.database.controllers;

import com.vrana.database.domain.dto.AuthorDto;
import com.vrana.database.services.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Authors", description = "Operations related to managing authors")
public class AuthorController {

    private final AuthorService authorService;

    @Operation(summary = "Create a new author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Author created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content) })
    @PostMapping(path = "/authors")
    public ResponseEntity<AuthorDto> createAuthor(
            @Valid @RequestBody AuthorDto authorDto) {
        return new ResponseEntity<>(authorService.createAuthor(authorDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Get a list of all authors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of authors",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorDto.class))}) })
    @GetMapping(path = "/authors")
    public List<AuthorDto> listAuthors() {
        return authorService.findAllAuthors();
    }

    @Operation(summary = "Get an author by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author found successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content) })
    @GetMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> getAuthor(
            @Parameter(
                    description = "id of author to be searched",
                    required = true)
            @PathVariable("id") Long id) {
        return new ResponseEntity<>(authorService.findOneOrThrow(id), HttpStatus.OK);
    }

    @Operation(summary = "Completely update an author",
            description = "Updates all fields of an existing author. All fields are required.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author updated successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content) })
    @PutMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> fullUpdateAuthor(
            @Parameter(
                    description = "id of author to be searched",
                    required = true)
            @PathVariable("id") Long id,
            @RequestBody AuthorDto authorDto) {
        return new ResponseEntity<>(authorService.updateFullAuthor(id, authorDto), HttpStatus.OK);
    }

    @Operation(summary = "Partially update an author",
            description = "Updates only the fields that are present in the request body.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author updated successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content) })
    @PatchMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDto> partialUpdateAuthor(
            @Parameter(
                    description = "id of author to be updated",
                    required = true)
            @PathVariable("id") Long id,
            @RequestBody AuthorDto authorDto) {
        return new ResponseEntity<>(authorService.updatePartialAuthor(id, authorDto), HttpStatus.OK);
    }

    @Operation(summary = "Delete an author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Author deleted successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorDto.class)) })})
    @DeleteMapping(path = "authors/{id}")
    public ResponseEntity<HttpStatus> deleteAuthor(
            @Parameter(
                    description = "id of author to be deleted",
                    required = true)
            @PathVariable("id") Long id) {
        authorService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
