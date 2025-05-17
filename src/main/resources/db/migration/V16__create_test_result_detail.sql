
CREATE TABLE test_result_detail (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    result_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    selected_answer_id BIGINT,
    is_correct BOOLEAN NOT NULL,
    CONSTRAINT fk_detail_result
        FOREIGN KEY (result_id)
        REFERENCES test_result(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_detail_question
        FOREIGN KEY (question_id)
        REFERENCES questions(id)
        ON DELETE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_detail_result_id ON test_result_detail(result_id);
