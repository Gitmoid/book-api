CREATE TABLE authors
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    age  INTEGER
);

CREATE TABLE books
(
    id        BIGSERIAL PRIMARY KEY,
    isbn      VARCHAR(255) UNIQUE,
    title     VARCHAR(255),
    author_id BIGINT,
    FOREIGN KEY (author_id) REFERENCES authors (id)
);