create table entity_value_jsonb
(
    id       varchar(255) primary key,
    payload  jsonb not null,
    registrationObjectDbk integer
);
