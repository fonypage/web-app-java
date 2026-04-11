-- V14__insert_practice_variables.sql
-- Практика по теме "Переменные и типы данных в Java"

INSERT INTO topic (title, content, type, video_url) VALUES
(
  'Практика: Объявление и использование переменных',
  '<p>В этом упражнении вы закрепите навыки работы с переменными и типами данных в Java. Вам предстоит объявить переменные всех базовых типов, присвоить им осмысленные значения и вывести результат работы программы на консоль.</p>

  <p><strong>Задача:</strong> Создайте класс <code>VariableDemo</code> с методом <code>main</code>. Объявите переменные следующих типов: <code>byte</code>, <code>short</code>, <code>int</code>, <code>long</code>, <code>float</code>, <code>double</code>, <code>char</code>, <code>boolean</code>. Присвойте им любые значения и выведите их в формате <code>Тип: значение</code>.</p>

  <h3>Шаблон кода</h3>
  <pre><code>public class VariableDemo {
    public static void main(String[] args) {
        byte b = 100;
        short s = 20000;
        int i = 123456;
        long l = 123456789L;
        float f = 3.14f;
        double d = 2.718281828;
        char c = ''J'';
        boolean bool = true;

        System.out.println(\"byte: \" + b);
        System.out.println(\"short: \" + s);
        System.out.println(\"int: \" + i);
        System.out.println(\"long: \" + l);
        System.out.println(\"float: \" + f);
        System.out.println(\"double: \" + d);
        System.out.println(\"char: \" + c);
        System.out.println(\"boolean: \" + bool);
    }
}</code></pre>',
  'practice',
  NULL
);