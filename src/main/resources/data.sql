insert into author (id, first_name, last_name) values
('a:1','Frank','Herbert'),
('a:2','Robert','Heinlein');


insert into book (id, name, page_count, author_id, prev_book_id) values
('b:1','Dune','300', 'a:1', null),
('b:2','Children of Dune','300', 'a:1', 'b:1');