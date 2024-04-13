CREATE TABLE authors
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE books
(
    id        SERIAL PRIMARY KEY,
    title     VARCHAR(100) NOT NULL,
    author_id BIGINT,
    FOREIGN KEY (author_id) REFERENCES authors (id)
);

CREATE TABLE categories
(
    id            SERIAL PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL
);

CREATE TABLE book2category
(
    book_id     BIGINT,
    category_id BIGINT,
    PRIMARY KEY (book_id, category_id),
    FOREIGN KEY (book_id) REFERENCES books (id),
    FOREIGN KEY (category_id) REFERENCES categories (id)
);

ALTER TABLE books
    ADD CONSTRAINT book_author_cnstr UNIQUE (title, author_id);

