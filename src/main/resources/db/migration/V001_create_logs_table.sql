CREATE SCHEMA IF NOT EXISTS news_feed

CREATE TABLE logs (
    id UUID PRIMARY KEY,
    status INT,
    exception VARCHAR(255),
    method VARCHAR(255),
    uri VARCHAR(255),
    use_id VARCHAR(255)
);
