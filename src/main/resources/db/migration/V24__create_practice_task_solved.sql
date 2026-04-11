CREATE TABLE practice_task_solved (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(255) NOT NULL,
  task_id BIGINT NOT NULL,
  solved_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_solved_task
    FOREIGN KEY (task_id) REFERENCES practice_task(id),
  UNIQUE KEY uk_solved_user_task (username, task_id),
  INDEX idx_solved_user (username)
) ENGINE=InnoDB;