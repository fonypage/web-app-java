-- V4__add_default_to_taken_at.sql
ALTER TABLE test_result
  MODIFY COLUMN taken_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
