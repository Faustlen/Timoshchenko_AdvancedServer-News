CREATE SCHEMA IF NOT EXISTS news_feed

CREATE TABLE news_tags (
    news_id BIGINT NOT NULL,
    tags_id BIGINT NOT NULL,
    PRIMARY KEY (news_id, tags_id),
    FOREIGN KEY (news_id) REFERENCES News(id),
    FOREIGN KEY (tags_id) REFERENCES Tags(id)
);