create table comments(
  id serial not null primary key,
  message text,
  parent_id integer references comments(id)
);