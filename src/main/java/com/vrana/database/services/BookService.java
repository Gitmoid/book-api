package com.vrana.database.services;

import com.vrana.database.domain.entities.BookEntity;

public interface BookService {

    BookEntity createBook(String isbn, BookEntity book);
}
