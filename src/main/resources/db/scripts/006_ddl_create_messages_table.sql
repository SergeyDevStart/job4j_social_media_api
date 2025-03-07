CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY,
    message TEXT NOT NULL,
    created TIMESTAMP DEFAULT NOW(),
    user_from_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    user_to_id BIGINT REFERENCES users(id) ON DELETE CASCADE
);