CREATE TABLE friendships (
    id BIGSERIAL PRIMARY KEY,
    user_send_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    user_accept_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE (user_send_id, user_accept_id)
);