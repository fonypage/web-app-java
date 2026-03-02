-- База у тебя java_learning (из application.properties)
ALTER DATABASE java_learning
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- Конкретно таблицу профиля тоже конвертируем
ALTER TABLE user_profile
  CONVERT TO CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- И на всякий случай явно для колонки institution
ALTER TABLE user_profile
  MODIFY institution VARCHAR(255)
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci
  NULL;