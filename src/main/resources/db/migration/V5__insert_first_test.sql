-- Вопросы к теме ID 1 — Переменные и типы данных в Java
INSERT INTO questions (topic_id, question) VALUES
  (1, 'Что такое переменная в Java?'),
  (1, 'Какой тип используется для хранения целых чисел?'),
  (1, 'Какие примитивные типы данных существуют в Java?'),
  (1, 'Сколько бит занимает тип double?'),
  (1, 'Какой оператор используется для сравнения равенства?')
;

-- Ответы для этих вопросов (question_id = 1…5)
INSERT INTO answers (question_id, text, is_correct) VALUES

  -- Варианты для вопроса ID 1
  (1, 'Это способ хранения данных с именем и типом.', true),
  (1, 'Это метод внутри класса.',           false),
  (1, 'Это оператор.',                      false),

  -- Варианты для вопроса ID 2
  (2, 'String', false),
  (2, 'int',    true),
  (2, 'float',  false),

  -- Варианты для вопроса ID 3
  (3, 'int, double, boolean, char',           true),
  (3, 'Integer, Double, Boolean, Character',  false),
  (3, 'string, number, boolean, char',        false),

  -- Варианты для вопроса ID 4
  (4, '32 бита', false),
  (4, '64 бита', true),
  (4, '16 бит',  false),

  -- Варианты для вопроса ID 5
  (5, '=',       false),
  (5, '==',      true),
  (5, 'equals()', false)
;
