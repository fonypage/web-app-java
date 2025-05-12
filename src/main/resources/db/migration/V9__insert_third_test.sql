INSERT INTO questions (topic_id, question) VALUES
  (3, 'Какие типы циклов существуют в Java?'),
  (3, 'Как работает цикл for?'),
  (3, 'В чём отличие цикла while от do-while?'),
  (3, 'Как записать бесконечный цикл?'),
  (3, 'Как досрочно выйти из цикла?')
;

-- Ответы для этих вопросов (question_id = 11…15)
INSERT INTO answers (question_id, text, is_correct) VALUES

  -- Варианты для вопроса ID 11
  (11, 'for, while, do-while',                TRUE),
  (11, 'if, switch, for',                     FALSE),
  (11, 'try, catch, finally',                 FALSE),

  -- Варианты для вопроса ID 12
  (12, 'Выполняет блок кода заданное число раз', TRUE),
  (12, 'Выполняется, пока условие ложно',        FALSE),
  (12, 'Выполняется хотя бы один раз',            FALSE),

  -- Варианты для вопроса ID 13
  (13, 'while проверяет условие перед телом, do-while — после', TRUE),
  (13, 'do-while проверяет перед телом, while — после',          FALSE),
  (13, 'while всегда выполняется хотя бы один раз',             FALSE),

  -- Варианты для вопроса ID 14
  (14, 'for(;;){…}',       TRUE),
  (14, 'while(true){…}',   FALSE),
  (14, 'if(true){…}',      FALSE),

  -- Варианты для вопроса ID 15
  (15, 'break',           TRUE),
  (15, 'continue',        FALSE),
  (15, 'return',          FALSE)
;