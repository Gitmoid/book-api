package com.vrana.database.openlibrary.controller;

import com.vrana.database.openlibrary.dto.OpenBookResponse;
import com.vrana.database.openlibrary.services.OpenService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OpenController {

    private final OpenService openService;

    @GetMapping(path = "/openbooks/{isbn}")
    public ResponseEntity<OpenBookResponse> createOpenBook(
            @Parameter(
                    description = "book isbn",
                    required = true)
            @PathVariable("isbn") String isbn) {

        return new ResponseEntity<>(openService.getOpenBookByIsbn(isbn), HttpStatus.OK);
    }
}
