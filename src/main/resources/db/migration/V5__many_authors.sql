CREATE TABLE book_author
(
    author_id BIGINT       NOT NULL,
    book_isbn VARCHAR(255) NOT NULL,
    CONSTRAINT pk_book_author PRIMARY KEY (author_id, book_isbn)
);

ALTER TABLE book_author
    ADD CONSTRAINT fk_booaut_on_author_entity FOREIGN KEY (author_id) REFERENCES authors (id);

ALTER TABLE book_author
    ADD CONSTRAINT fk_booaut_on_book_entity FOREIGN KEY (book_isbn) REFERENCES books (isbn);

ALTER TABLE books
    ALTER COLUMN isbn SET NOT NULL;

ALTER TABLE books
    DROP COLUMN author_id;