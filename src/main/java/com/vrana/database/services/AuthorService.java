package com.vrana.database.services;

import com.vrana.database.domain.entities.AuthorEntity;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    AuthorEntity createAuthor(AuthorEntity author);

    List<AuthorEntity> findAll();

    Optional<AuthorEntity> findOne(Long id);
}
