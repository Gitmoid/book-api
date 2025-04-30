package com.vrana.database.services;

import com.vrana.database.domain.dto.AuthorDto;

import java.util.List;

public interface AuthorService {
    AuthorDto createAuthor(AuthorDto authorDto);

    List<AuthorDto> findAllAuthors();

    AuthorDto findOneOrThrow(Long id);

    AuthorDto updateFullAuthor(Long id, AuthorDto authorDto);

    AuthorDto updatePartialAuthor(Long id, AuthorDto authorDto);

    void delete(Long id);
}
