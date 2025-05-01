package com.vrana.database.services;

import com.vrana.database.domain.dto.BookDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto createBook(String isbn, BookDto bookDto);

    Page<BookDto> findAllBooks(Pageable pageable);

    BookDto findOneOrThrow(String isbn);

    BookDto updateFullBook(String isbn, BookDto bookDto);

    BookDto updatePartialBook(String isbn, BookDto bookDto);

    void delete(String isbn);
}
