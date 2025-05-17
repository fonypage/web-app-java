CREATE TABLE test_result (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    topic_id BIGINT NOT NULL,
    username VARCHAR(255) NOT NULL,
    correct_count INT NOT NULL,
    total_count INT NOT NULL,
    taken_at DATETIME NOT NULL,
    CONSTRAINT fk_test_result_topic
        FOREIGN KEY (topic_id)
        REFERENCES topic(id)
        ON DELETE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_test_result_username ON test_result(username);
