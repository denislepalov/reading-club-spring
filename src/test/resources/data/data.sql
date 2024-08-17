DELETE FROM author_book;
DELETE FROM authors;
DELETE FROM books;
DELETE FROM readers;

ALTER TABLE authors ALTER COLUMN id RESTART WITH 1;
ALTER TABLE books ALTER COLUMN id RESTART WITH 1;
ALTER TABLE readers ALTER COLUMN id RESTART WITH 1;




INSERT INTO readers (name, surname, phone, address)
VALUES ('Ivan', 'Ivanov', '71111111111', 'Lenina 11'),
       ('Petr', 'Petrov', '72222222222', 'Lenina 22'),
       ('Sveta', 'Svetikova', '73333333333', 'Lenina 33');


INSERT INTO books (title, inventory_number, reader_id)
VALUES ('Title1', 11111, 1),
       ('Title2', 22222, 2),
       ('Title3', 33333, 3);

INSERT INTO authors (full_name, personal_info)
VALUES ('Author1', 'likes dogs'),
       ('Author2', 'likes cats'),
       ('Author3', 'likes wolfs');

INSERT INTO author_book (author_id, book_id)
VALUES (1, 1),
       (3, 1),
       (2, 3),
       (3, 3),
       (2, 2);





