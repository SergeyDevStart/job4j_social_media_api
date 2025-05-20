CREATE TABLE users_roles (
    role_id BIGINT REFERENCES roles(id),
    user_id BIGINT REFERENCES users(id)
);