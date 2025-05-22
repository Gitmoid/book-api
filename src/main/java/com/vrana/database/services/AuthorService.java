package com.vrana.database.services;

import com.vrana.database.domain.dto.AuthorDto;

import java.util.List;

public interface AuthorService {
    AuthorDto createAuthor(AuthorDto authorDto);

    AuthorDto getOrCreateAuthor(AuthorDto authorDto);

    AuthorDto createOpenAuthor(String authorKey);

    AuthorDto getOrCreateOpenAuthor(String authorKey);

    List<AuthorDto> findAllAuthors();

    AuthorDto findOneOrThrow(Long id);

    AuthorDto updateFullAuthor(Long id, AuthorDto authorDto);

    AuthorDto updatePartialAuthor(Long id, AuthorDto authorDto);

    void delete(Long id);
}