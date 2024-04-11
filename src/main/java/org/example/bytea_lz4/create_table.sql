drop table IF EXISTS entity_value_bytea_lz4;
create table entity_value_bytea_lz4
(
    id       varchar(255) primary key,
    payload  bytea COMPRESSION lz4 not null,
    registrationObjectDbk integer
);