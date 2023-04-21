create table Person (
  id bigserial primary key,
  name varchar(400) not null,
  age integer not null,
  dateOfBirth date,
  created timestamp,
  modified timestamp,
  alive boolean,
  maritalStatus varchar(200)
)
