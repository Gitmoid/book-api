
CREATE SEQUENCE IF NOT EXISTS author_id_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE IF NOT EXISTS authors (
    id BIGINT PRIMARY KEY DEFAULT nextval('author_id_seq'),
    name VARCHAR(255),
    age INTEGER
);

CREATE TABLE IF NOT EXISTS books (
    isbn VARCHAR(255) PRIMARY KEY,
    title VARCHAR(255),
    author_id BIGINT,
    FOREIGN KEY (author_id) REFERENCES authors(id)
);