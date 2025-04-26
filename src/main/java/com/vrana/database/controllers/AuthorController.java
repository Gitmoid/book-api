package com.vrana.database.controllers;

import com.vrana.database.domain.dto.AuthorDto;
import com.vrana.database.domain.entities.AuthorEntity;
import com.vrana.database.mappers.Mapper;
import com.vrana.database.services.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Authors", description = "Operations related to managing authors")
public class AuthorController {

    private AuthorService authorService;
    private Mapper<AuthorEntity, AuthorDto> authorMapper;

    public AuthorController(AuthorService authorService, Mapper<AuthorEntity, AuthorDto> authorMapper) {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
    }

    @Operation(summary = "Create a new author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Author created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content) })
    @PostMapping(path = "/authors")
    public ResponseEntity<AuthorDto> createAuthor(
            @RequestBody AuthorDto author) {
        AuthorEntity authorEntity = authorMapper.mapFrom(author);
        AuthorEntity savedAuthorEntity = authorService.saveAuthor(authorEntity);
        return new ResponseEntity<>(authorMapper.mapTo(savedAuthorEntity), HttpStatus.CREATED);
    }

    @Operation(summary = "Get a list of all authors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of authors",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorDto.class))}) })
    @GetMapping(path = "/authors")
    public List<AuthorDto> listAuthors() {
        List<AuthorEntity> authors = authorService.findAll();
        return authors.stream()
                .map(authorMapper::mapTo)
                .collect(Collectors.toList());
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
        Optional<AuthorEntity> foundAuthor = authorService.findOne(id);
        return foundAuthor.map(authorEntity -> {
            AuthorDto authorDto = authorMapper.mapTo(authorEntity);
            return new ResponseEntity<>(authorDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
        if(!authorService.isExists(id)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        authorDto.setId(id);
        AuthorEntity authorEntity = authorMapper.mapFrom(authorDto);
        AuthorEntity savedAuthorEntity = authorService.saveAuthor(authorEntity);
        return new ResponseEntity<>(authorMapper.mapTo(savedAuthorEntity), HttpStatus.OK);
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
        if (!authorService.isExists(id)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        AuthorEntity authorEntity = authorMapper.mapFrom(authorDto);
        AuthorEntity updatedAuthor = authorService.partialUpdate(id, authorEntity);
        return new ResponseEntity<>(authorMapper.mapTo(updatedAuthor), HttpStatus.OK);
    }

    @Operation(summary = "Delete an author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Author deleted successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthorDto.class)) })})
    @DeleteMapping(path = "authors/{id}")
    public ResponseEntity deleteAuthor(
            @Parameter(
                    description = "id of author to be deleted",
                    required = true)
            @PathVariable("id") Long id) {
        authorService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}