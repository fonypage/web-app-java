-- Удаляем только две старые задачи, которые мы добавляли:
-- 1) "Сумма двух чисел"
-- 2) "Сумма двух чисел (JUnit)"

CREATE TEMPORARY TABLE tmp_task_ids (id BIGINT PRIMARY KEY);

INSERT INTO tmp_task_ids(id)
SELECT id
FROM practice_task
WHERE title IN ('Сумма двух чисел', 'Сумма двух чисел (JUnit)');

-- Чистим зависимые записи
DELETE FROM practice_testcase WHERE task_id IN (SELECT id FROM tmp_task_ids);
DELETE FROM practice_submission WHERE task_id IN (SELECT id FROM tmp_task_ids);
DELETE FROM practice_task_solved WHERE task_id IN (SELECT id FROM tmp_task_ids);

-- Удаляем сами задачи
DELETE FROM practice_task WHERE id IN (SELECT id FROM tmp_task_ids);

DROP TEMPORARY TABLE tmp_task_ids;