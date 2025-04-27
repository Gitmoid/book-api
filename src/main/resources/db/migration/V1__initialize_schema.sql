CREATE SEQUENCE author_id_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE book_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE authors
(
    id   BIGINT PRIMARY KEY DEFAULT nextval('author_id_seq'),
    name VARCHAR(255),
    age  INTEGER
);

CREATE TABLE books
(
    id        BIGINT PRIMARY KEY DEFAULT nextval('book_id_seq'),
    isbn      VARCHAR(255) UNIQUE,
    title     VARCHAR(255),
    author_id BIGINT,
    FOREIGN KEY (author_id) REFERENCES authors (id)
);