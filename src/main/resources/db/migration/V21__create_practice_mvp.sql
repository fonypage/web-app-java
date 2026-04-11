CREATE TABLE practice_task (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  topic_id BIGINT NOT NULL,
  title VARCHAR(255) NOT NULL,
  description_html TEXT NOT NULL,
  starter_code TEXT NOT NULL,
  time_limit_ms INT NOT NULL DEFAULT 2000,
  memory_mb INT NOT NULL DEFAULT 256,
  xp_reward INT NOT NULL DEFAULT 50,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_practice_task_topic
    FOREIGN KEY (topic_id) REFERENCES topic(id)
) ENGINE=InnoDB;

CREATE TABLE practice_testcase (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  task_id BIGINT NOT NULL,
  input_text TEXT NOT NULL,
  expected_text TEXT NOT NULL,
  is_hidden BOOLEAN NOT NULL DEFAULT FALSE,
  CONSTRAINT fk_practice_testcase_task
    FOREIGN KEY (task_id) REFERENCES practice_task(id)
) ENGINE=InnoDB;

CREATE TABLE practice_submission (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(255) NOT NULL,
  task_id BIGINT NOT NULL,
  status VARCHAR(10) NOT NULL,       -- OK/WA/CE/TLE/RE
  passed_count INT NOT NULL DEFAULT 0,
  total_count INT NOT NULL DEFAULT 0,
  duration_ms INT NULL,
  last_stdout TEXT NULL,
  last_stderr TEXT NULL,
  code TEXT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_practice_submission_task
    FOREIGN KEY (task_id) REFERENCES practice_task(id),
  INDEX idx_practice_submission_user (username),
  INDEX idx_practice_submission_task (task_id)
) ENGINE=InnoDB;