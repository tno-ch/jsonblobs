drop table IF EXISTS entity_value_lob_lz4;
create table entity_value_lob_lz4
(
    id       varchar(255) primary key,
    payload  text COMPRESSION lz4 not null,
    registrationObjectDbk integer
);