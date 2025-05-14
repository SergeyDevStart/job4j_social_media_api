CREATE TABLE persons_roles (
    role_id BIGINT REFERENCES roles(id),
    person_id BIGINT REFERENCES persons(id)
);