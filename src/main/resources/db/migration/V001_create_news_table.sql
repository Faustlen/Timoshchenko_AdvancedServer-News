CREATE SCHEMA IF NOT EXISTS news_feed

CREATE TABLE news (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    image VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES Users(id)
);
