create table test_entity_value
(
    id       VARCHAR2(40 CHAR) primary key,
    payload  blob not null,
    registrationObjectDbk NUMBER(8,0)
);

GRANT SELECT, INSERT, UPDATE, DELETE ON BRO_DBA.TEST_ENTITY_VALUE TO BRO_SERVICES_USER;