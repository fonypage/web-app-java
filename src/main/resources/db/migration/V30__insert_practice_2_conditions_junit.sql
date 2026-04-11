INSERT INTO topic (title, content, type, video_url)
VALUES (
  'Практика: Условия if/else',
  '<p>Научитесь писать простую логику ветвления.</p>
   <p><strong>Задача:</strong> реализуйте метод <code>sign(int x)</code>.</p>',
  'practice',
  NULL
);

SET @topic_id = LAST_INSERT_ID();

INSERT INTO practice_task(
  topic_id, title, description_html, starter_code,
  time_limit_ms, memory_mb, xp_reward,
  checker_type, junit_test_code
)
VALUES (
  @topic_id,
  'Условия: знак числа',
  '<p>Реализуйте метод <code>sign(int x)</code>:</p>
   <ul>
     <li>если x &gt; 0 → <code>POSITIVE</code></li>
     <li>если x &lt; 0 → <code>NEGATIVE</code></li>
     <li>если x == 0 → <code>ZERO</code></li>
   </ul>',
  CONCAT(
    'public class Solution {\n',
    '  public static String sign(int x) {\n',
    '    // TODO\n',
    '    return \"\";\n',
    '  }\n',
    '}\n'
  ),
  2000,
  256,
  60,
  'JUNIT',
  CONCAT(
    'import org.junit.jupiter.api.*;\n',
    'import static org.junit.jupiter.api.Assertions.*;\n\n',
    'public class TaskTest {\n',
    '  @Test void pos(){ assertEquals(\"POSITIVE\", Solution.sign(7)); }\n',
    '  @Test void neg(){ assertEquals(\"NEGATIVE\", Solution.sign(-3)); }\n',
    '  @Test void zero(){ assertEquals(\"ZERO\", Solution.sign(0)); }\n',
    '  @Test void edge(){\n',
    '    assertEquals(\"POSITIVE\", Solution.sign(1));\n',
    '    assertEquals(\"NEGATIVE\", Solution.sign(-1));\n',
    '  }\n',
    '}\n'
  )
);