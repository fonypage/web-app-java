INSERT INTO practice_task(topic_id, title, description_html, starter_code, time_limit_ms, memory_mb, xp_reward, checker_type, junit_test_code)
VALUES (
  2,
  'Сумма двух чисел (JUnit)',
  '<p>Реализуйте метод <code>add(int a, int b)</code>, который возвращает сумму.</p>',
  'public class Solution {\n  public static int add(int a, int b) {\n    // TODO\n    return 0;\n  }\n}\n',
  2000,
  256,
  80,
  'JUNIT',
  'import org.junit.jupiter.api.*;\nimport static org.junit.jupiter.api.Assertions.*;\n\npublic class TaskTest {\n  @Test void sample1(){ assertEquals(5, Solution.add(2,3)); }\n  @Test void sample2(){ assertEquals(3, Solution.add(10,-7)); }\n}\n'
);