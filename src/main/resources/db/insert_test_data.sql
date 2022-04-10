insert into comments(message, parent_id)
values('first', null),
      ('second', 1),
      ('second first', 2),
      ('second second', 2),
      ('second second first', 3),
      ('third', 1),
      ('second thread', null);