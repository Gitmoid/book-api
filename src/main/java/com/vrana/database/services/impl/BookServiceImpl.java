package com.vrana.database.services.impl;

import com.vrana.database.domain.dto.BookDto;
import com.vrana.database.domain.entities.BookEntity;
import com.vrana.database.mappers.BookMapper;
import com.vrana.database.repositories.BookRepository;
import com.vrana.database.services.BookService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Override
    public BookDto createBook(String isbn, BookDto bookDto) {
        if (bookRepository.existsById(isbn)) {
            throw new EntityExistsException("Book with ISBN " + isbn + " already exists");
        }

        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        bookEntity.setIsbn(isbn);
        BookEntity savedBookEntity = bookRepository.save(bookEntity);

        return bookMapper.mapTo(savedBookEntity);
    }

    @Override
    public Page<BookDto> findAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                        .map(bookMapper::mapTo);
    }

    private BookEntity getBookEntityOrThrow(String isbn) {
        return bookRepository.findById(isbn)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with isbn: " + isbn));
    }

    @Override
    public BookDto findOneOrThrow(String isbn) {
        BookEntity bookEntity = getBookEntityOrThrow(isbn);

        return bookMapper.mapTo(bookEntity);
    }

    @Override
    public BookDto updateFullBook(String isbn, BookDto bookDto) {
        BookEntity existingBookEntity = getBookEntityOrThrow(isbn);
        bookMapper.updateFullBookFromDto(bookDto, existingBookEntity);
        BookEntity savedBookEntity = bookRepository.save(existingBookEntity);

        return bookMapper.mapTo(savedBookEntity);
    }

    @Override
    public BookDto updatePartialBook(String isbn, BookDto bookDto) {
        BookEntity existingBookEntity = getBookEntityOrThrow(isbn);
        bookMapper.updatePartialBookFromDto(bookDto, existingBookEntity);
        BookEntity savedBookEntity = bookRepository.save(existingBookEntity);

        return bookMapper.mapTo(savedBookEntity);
    }

    @Override
    public void delete(String isbn) {
        bookRepository.deleteById(isbn);
    }
}
