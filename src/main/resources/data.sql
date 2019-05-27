insert into author (id, first_name, last_name) values
('a:1','Frank','Herbert'),
('a:2','Robert','Heinlein');


insert into book (id, name, page_count, author, prev_book_id, popularity) values
('b:1','Dune','412', 'a:1', null, 50),
('b:2','Dune Messiah','256', 'a:1', 'b:1', 32),
('b:3','Children of Dune','444', 'a:1', 'b:2', 41),
('b:4','God Emperor of Dune','423', 'a:1', 'b:3', 80),
('b:5','Starship Troopers','263', 'a:2', null, 66);