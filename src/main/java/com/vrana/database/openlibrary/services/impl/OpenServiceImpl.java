package com.vrana.database.openlibrary.services.impl;

import com.vrana.database.exceptions.OpenResourceNotFoundException;
import com.vrana.database.openlibrary.dto.OpenAuthorResponse;
import com.vrana.database.openlibrary.dto.OpenBookResponse;
import com.vrana.database.openlibrary.services.OpenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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

        try {
            ResponseEntity<OpenBookResponse> response = openLibraryRestTemplate
                    .exchange(uri, HttpMethod.GET, null, OpenBookResponse.class);

            return response.getBody();
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new OpenResourceNotFoundException("Book with ISBN" + isbn + " not found in openlibrary");
            }

            throw ex;
        }
    }

    @Override
    public OpenAuthorResponse getOpenAuthorByKey(String authorKey) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://openlibrary.org/authors/{authorKey}.json")
                .build(authorKey);

        try {
            ResponseEntity<OpenAuthorResponse> response = openLibraryRestTemplate
                    .exchange(uri, HttpMethod.GET, null, OpenAuthorResponse.class);

            return response.getBody();
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new OpenResourceNotFoundException("Author with key " + authorKey + " not found in openlibrary");
            }

            throw ex;
        }
    }
}