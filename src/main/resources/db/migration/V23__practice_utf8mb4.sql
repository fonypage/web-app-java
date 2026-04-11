ALTER TABLE practice_submission
  CONVERT TO CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

ALTER TABLE practice_submission
  MODIFY code LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  MODIFY last_stdout LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  MODIFY last_stderr LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL;