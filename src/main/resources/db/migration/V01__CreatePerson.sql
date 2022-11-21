create table Person (
  id bigint primary key auto_increment,
  name varchar not null,
  age integer not null,
  dateOfBirth date,
  created timestamp,
  modified timestamp,
  alive boolean,
  maritalStatus varchar
)
