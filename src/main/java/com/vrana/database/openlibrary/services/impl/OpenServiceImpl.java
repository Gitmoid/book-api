package com.vrana.database.openlibrary.services.impl;

import com.vrana.database.openlibrary.dto.OpenAuthorResponse;
import com.vrana.database.openlibrary.dto.OpenBookResponse;
import com.vrana.database.openlibrary.services.OpenService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class OpenServiceImpl implements OpenService {

    private final RestTemplate openLibraryRestTemplate;

    @Override
    public OpenBookResponse getOpenBookByIsbn(String isbn) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://openlibrary.org/isbn/{isbn}.json")
                .build(isbn);

        ResponseEntity<OpenBookResponse> response = openLibraryRestTemplate
                .exchange(uri, HttpMethod.GET, null, OpenBookResponse.class);

        OpenBookResponse openBookResponse = response.getBody();

        if (openBookResponse == null) {
            throw new EntityNotFoundException("Book not found in openlibrary with isbn: " + isbn);
        }

        return openBookResponse;
    }

    @Override
    public OpenAuthorResponse getOpenAuthorByKey(String authorKey) {
        if (authorKey.isEmpty()) {
            return null;
        }

        URI uri = UriComponentsBuilder
                .fromUriString("https://openlibrary.org" + authorKey + ".json")
                .build()
                .toUri();

        ResponseEntity<OpenAuthorResponse> response = openLibraryRestTemplate
                .exchange(uri, HttpMethod.GET, null, OpenAuthorResponse.class);

        OpenAuthorResponse openAuthorResponse = response.getBody();

        if (openAuthorResponse == null) {
            throw new EntityNotFoundException("Author not found in openlibrary with key: " + authorKey);
        }

        return openAuthorResponse;
    }
}