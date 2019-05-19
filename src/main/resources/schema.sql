create table author (
    id varchar(64) PRIMARY KEY,
    last_name varchar(64) not null,
    first_name varchar(64) not null,
);
create table book (
    id varchar(64) PRIMARY KEY,
    name varchar(64) not null,
    page_count int not null,
    author_id varchar(64) not null,
    prev_book_id varchar(64)
  );