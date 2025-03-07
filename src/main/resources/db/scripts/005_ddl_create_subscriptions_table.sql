CREATE TABLE subscriptions (
    id BIGSERIAL PRIMARY KEY,
    subscriber_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    subscribed_to_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE (subscriber_id, subscribed_to_id)
);