INSERT INTO topic (title, content, type, video_url) VALUES
-- Теория
('Основы синтаксиса Java', 'В этом разделе рассказывается о базовых элементах синтаксиса Java: структура программы, имена переменных, типы данных.', 'theory', 'https://www.youtube.com/watch?v=grEKMHGYyns'),
('Условные операторы в Java', 'В этом разделе рассматриваются условные операторы if, if-else, switch в языке Java.', 'theory', 'https://www.youtube.com/watch?v=HgJ4hW45xgE'),

-- Практика
('Практика: Сложение двух чисел',
'Напишите программу, которая находит сумму двух чисел.',
'practice',
'int a = 5;\nint b = 10;\nint sum = a + b;\nSystem.out.println("Сумма: " + sum);'),

('Практика: Проверка чётности числа',
'Напишите программу, которая проверяет, является ли число чётным.',
'practice',
'int number = 7;\nif (number % 2 == 0) {\n    System.out.println("Число чётное");\n} else {\n    System.out.println("Число нечётное");\n}');
