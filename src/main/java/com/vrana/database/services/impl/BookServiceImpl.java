package com.vrana.database.services.impl;

import com.vrana.database.domain.entities.BookEntity;
import com.vrana.database.repositories.BookRepository;
import com.vrana.database.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public BookEntity createUpdateBook(BookEntity book) {
        return bookRepository.save(book);
    }

    @Override
    public List<BookEntity> findAll() {
        return new ArrayList<>(bookRepository.findAll());
    }

    @Override
    public Page<BookEntity> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public Optional<BookEntity> findOne(String isbn) {
        return bookRepository.findById(isbn);
    }

    @Override
    public boolean exists(String isbn) {
        return bookRepository.existsById(isbn);
    }

    @Override
    public BookEntity partialUpdate(BookEntity bookEntity) {
        BookEntity existingBook = bookRepository.getReferenceById(bookEntity.getIsbn());

        if (bookEntity.getTitle() != null) {
            existingBook.setTitle(bookEntity.getTitle());
        }

        return bookRepository.save(existingBook);
    }

    @Override
    public void delete(String isbn) {
        bookRepository.deleteById(isbn);
    }
}
