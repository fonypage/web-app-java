-- 1) Таблица «прохождений» теста
CREATE TABLE test_results (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  session_id VARCHAR(100) NOT NULL,       -- ваш идентификатор из куки
  topic_id BIGINT NOT NULL,               -- к какой теме был тест
  correct_count INT NOT NULL,              -- сколько правильных
  total_count INT NOT NULL,                -- всего вопросов
  taken_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (topic_id) REFERENCES topic(id) ON DELETE CASCADE,
  INDEX idx_session_id(session_id)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

-- 2) Таблица «ответов» в рамках одного прохождения
CREATE TABLE test_answers (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  result_id BIGINT NOT NULL,               -- ссылка на test_results.id
  question_id BIGINT NOT NULL,             -- на какой вопрос отвечали
  answer_id BIGINT,                        -- выбранный вариант (может быть NULL)
  is_correct BOOLEAN NOT NULL,             -- сразу пометим правильность
  FOREIGN KEY (result_id) REFERENCES test_results(id) ON DELETE CASCADE,
  FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE,
  FOREIGN KEY (answer_id)   REFERENCES answers(id)   ON DELETE SET NULL
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;
