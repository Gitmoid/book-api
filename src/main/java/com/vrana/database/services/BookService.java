package com.vrana.database.services;

import com.vrana.database.domain.entities.BookEntity;

import java.util.List;

public interface BookService {

    BookEntity createBook(String isbn, BookEntity book);


    List<BookEntity> findAll();
}
