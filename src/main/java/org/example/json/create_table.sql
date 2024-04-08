create table entity_value_json
(
    id       varchar(255) primary key,
    payload  json not null,
    registrationObjectDbk integer
);