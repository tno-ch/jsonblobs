create table entity_value_json_lz4
(
    id       varchar(255) primary key,
    payload  json COMPRESSION lz4 not null,
    registrationObjectDbk integer
);