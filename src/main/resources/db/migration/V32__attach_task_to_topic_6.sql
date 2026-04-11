INSERT INTO practice_task(
  topic_id, title, description_html, starter_code,
  time_limit_ms, memory_mb, xp_reward,
  checker_type, junit_test_code
)
VALUES (
  6,
  'Переменные: вывод значений',
  '<p>Реализуйте метод <code>format()</code>, который возвращает строки с переменными базовых типов.</p>
   <p>Формат (каждое с новой строки):</p>
   <pre><code>byte: 100
short: 20000
int: 123456
long: 123456789
float: 3.14
double: 2.718281828
char: J
boolean: true
</code></pre>',
  CONCAT(
    'public class Solution {\n',
    '  public static String format() {\n',
    '    // TODO: return required lines\n',
    '    return \"\";\n',
    '  }\n',
    '}\n'
  ),
  2000,
  256,
  80,
  'JUNIT',
  CONCAT(
    'import org.junit.jupiter.api.*;\n',
    'import static org.junit.jupiter.api.Assertions.*;\n\n',
    'public class TaskTest {\n',
    '  @Test void testOutput(){\n',
    '    String s = Solution.format();\n',
    '    assertNotNull(s);\n',
    '    s = s.replace(\"\\r\\n\",\"\\n\");\n',
    '    assertTrue(s.contains(\"byte: 100\"));\n',
    '    assertTrue(s.contains(\"short: 20000\"));\n',
    '    assertTrue(s.contains(\"int: 123456\"));\n',
    '    assertTrue(s.contains(\"long: 123456789\"));\n',
    '    assertTrue(s.contains(\"float: 3.14\"));\n',
    '    assertTrue(s.contains(\"double: 2.718281828\"));\n',
    '    assertTrue(s.contains(\"char: J\"));\n',
    '    assertTrue(s.contains(\"boolean: true\"));\n',
    '  }\n',
    '}\n'
  )
);