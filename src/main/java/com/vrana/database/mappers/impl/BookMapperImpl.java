package com.vrana.database.mappers.impl;

import com.vrana.database.domain.dto.BookDto;
import com.vrana.database.domain.entities.BookEntity;
import com.vrana.database.mappers.BookMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookMapperImpl implements BookMapper<BookEntity, BookDto> {

    private final ModelMapper modelMapper;

    @Override
    public BookDto mapTo(BookEntity book) {
        return modelMapper.map(book, BookDto.class);
    }

    @Override
    public BookEntity mapFrom(BookDto bookDto, String isbn) {
        bookDto.setIsbn(isbn);
        return modelMapper.map(bookDto, BookEntity.class);
    }
}
