CREATE TABLE logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    status INT,
    exception VARCHAR(255),
    method VARCHAR(255),
    uri VARCHAR(255),
    use_id VARCHAR(255)
);
