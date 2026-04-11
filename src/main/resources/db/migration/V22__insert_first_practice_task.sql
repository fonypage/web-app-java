INSERT INTO practice_task(topic_id, title, description_html, starter_code, time_limit_ms, memory_mb, xp_reward)
VALUES (
  1,
  'Сумма двух чисел',
  '<p>Считай из входа два целых числа и верни их сумму.</p>
   <p><b>Формат ввода:</b> два числа через пробел или перевод строки.</p>
   <p><b>Формат вывода:</b> одно число — сумма.</p>',
  'public class Solution {\n  public static String solve(String input) {\n    // TODO: напиши решение\n    return \"\";\n  }\n}\n',
  2000,
  256,
  50
);

SET @task_id = LAST_INSERT_ID();

INSERT INTO practice_testcase(task_id, input_text, expected_text, is_hidden)
VALUES
(@task_id, '2 3\n', '5\n', FALSE),
(@task_id, '10\n-7\n', '3\n', TRUE);