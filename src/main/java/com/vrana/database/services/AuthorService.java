package com.vrana.database.services;

import com.vrana.database.domain.entities.AuthorEntity;

import java.util.List;

public interface AuthorService {
    AuthorEntity createAuthor(AuthorEntity author);

    List<AuthorEntity> findAll();
}
