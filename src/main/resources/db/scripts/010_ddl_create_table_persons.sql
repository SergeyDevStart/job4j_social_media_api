CREATE TABLE persons (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(120) NOT NULL,
    username VARCHAR(20) NOT NULL UNIQUE
);