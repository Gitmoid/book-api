package com.vrana.database;

import com.vrana.database.domain.dto.AuthorDto;
import com.vrana.database.domain.dto.BookDto;
import com.vrana.database.domain.entities.AuthorEntity;
import com.vrana.database.domain.entities.BookEntity;

public final class TestDataUtil {
    private TestDataUtil() {
    }

    public static AuthorEntity createTestAuthorEntityA() {
        AuthorEntity author = new AuthorEntity();
        author.setId(1L);
        author.setName("Abigail Rose");
        author.setAge(80);
        return author;
    }

    public static AuthorEntity createTestAuthorEntityB() {
        AuthorEntity author = new AuthorEntity();
        author.setId(1L);
        author.setName("Thomas Cronin");
        author.setAge(44);
        return author;
    }

    public static AuthorEntity createTestAuthorEntityC() {
        AuthorEntity author = new AuthorEntity();
        author.setId(3L);
        author.setName("Jesse A Casey");
        author.setAge(24);
        return author;
    }

    public static AuthorDto createTestAuthorDtoA() {
        AuthorDto author = new AuthorDto();
        author.setName("Abigail Rose");
        author.setAge(80);
        return author;
    }

    public static AuthorDto createTestAuthorDtoB() {
        AuthorDto author = new AuthorDto();
        author.setName("Thomas Cronin");
        author.setAge(44);
        return author;
    }

    public static AuthorDto createTestAuthorDtoC() {
        AuthorDto author = new AuthorDto();
        author.setId(3L);
        author.setName("Jesse A Casey");
        author.setAge(24);
        return author;
    }

    public static BookEntity createTestBookEntityA(final AuthorEntity authorEntity) {
        BookEntity book = new BookEntity();
        book.setIsbn("978-1-2345-6789-0");
        book.setTitle("The Shadow in the Attic");
        book.setAuthor(authorEntity);
        return book;
    }

    public static BookEntity createTestBookEntityB(final AuthorEntity authorEntity) {
        BookEntity book = new BookEntity();
        book.setIsbn("978-1-2345-6789-1");
        book.setTitle("Beyond the Horizon");
        book.setAuthor(authorEntity);
        return book;
    }

    public static BookEntity createTestBookEntityC(final AuthorEntity authorEntity) {
        BookEntity book = new BookEntity();
        book.setIsbn("978-1-2345-6789-2");
        book.setTitle("The Last Ember");
        book.setAuthor(authorEntity);
        return book;
    }

    public static BookDto createTestBookDtoA(final AuthorDto authorDto) {
        BookDto book = new BookDto();
        book.setIsbn("978-1-2345-6789-0");
        book.setTitle("The Shadow in the Attic");
        book.setAuthor(authorDto);
        return book;
    }

    public static BookDto createTestBookDtoB(final AuthorDto authorDto) {
        BookDto book = new BookDto();
        book.setIsbn("978-1-2345-6789-1");
        book.setTitle("Beyond the Horizon");
        book.setAuthor(authorDto);
        return book;
    }

    public static BookDto createTestBookDtoC(final AuthorDto authorDto) {
        BookDto book = new BookDto();
        book.setIsbn("978-1-2345-6789-2");
        book.setTitle("The Last Ember");
        book.setAuthor(authorDto);
        return book;
    }
}