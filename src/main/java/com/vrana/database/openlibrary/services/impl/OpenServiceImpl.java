package com.vrana.database.openlibrary.services.impl;

import com.vrana.database.openlibrary.dto.OpenBookResponse;
import com.vrana.database.openlibrary.services.OpenService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
}