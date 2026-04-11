CREATE TABLE achievement (
  code VARCHAR(64) PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description VARCHAR(500) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE user_achievement (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(255) NOT NULL,
  achievement_code VARCHAR(64) NOT NULL,
  earned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_user_ach (username, achievement_code),
  CONSTRAINT fk_user_ach_code FOREIGN KEY (achievement_code) REFERENCES achievement(code)
) ENGINE=InnoDB;

INSERT INTO achievement(code,title,description) VALUES
('PRACTICE_FIRST','Первая решённая задача','Вы решили первую практическую задачу.'),
('PRACTICE_3','Три задачи решено','Вы решили 3 практические задачи.'),
('PRACTICE_5','Пять задач решено','Вы решили 5 практических задач.'),
('PRACTICE_10','Десять задач решено','Вы решили 10 практических задач.'),
('PRACTICE_FIRST_TRY','С первого раза','Вы решили задачу с первой попытки.');