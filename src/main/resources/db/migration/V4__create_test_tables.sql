CREATE TABLE questions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    topic_id BIGINT,
    question TEXT NOT NULL,
    FOREIGN KEY (topic_id) REFERENCES topic(id) ON DELETE CASCADE
)ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE TABLE answers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_id BIGINT,
    text TEXT NOT NULL,
    is_correct BOOLEAN NOT NULL,
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
)ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;