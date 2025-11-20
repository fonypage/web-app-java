-- Отели
INSERT INTO hotel (name, city, description, rating, image_url) VALUES
  ('Гранд Отель Москва', 'Москва',
   'Современный отель в центре Москвы, рядом с метро и основными достопримечательностями.',
   4.7,
   'https://storage.yandexcloud.net/your-bucket/hotel-moscow.jpg'),
  ('Нева Палас', 'Санкт-Петербург',
   'Уютный отель возле Невы с видом на исторический центр города.',
   4.5,
   'https://storage.yandexcloud.net/your-bucket/hotel-spb.jpg'),
  ('Сочи Резорт', 'Сочи',
   'Курортный отель у моря с собственным пляжем и бассейном.',
   4.8,
   'https://storage.yandexcloud.net/your-bucket/hotel-sochi.jpg');

-- Номера для отеля 1 (Гранд Отель Москва)
INSERT INTO room (hotel_id, title, capacity, price_per_night, description, image_url) VALUES
  (1, 'Стандартный номер', 2, 4500.00,
   'Уютный номер с одной двуспальной кроватью, рабочим столом и Wi-Fi.',
   'https://storage.yandexcloud.net/your-bucket/room-moscow-standard.jpg'),
  (1, 'Улучшенный номер', 3, 6500.00,
   'Просторный номер с отдельной зоной отдыха, телевизором и кондиционером.',
   'https://storage.yandexcloud.net/your-bucket/room-moscow-comfort.jpg'),

-- Номера для отеля 2 (Нева Палас, СПб)
  (2, 'Номер с видом на Неву', 2, 7000.00,
   'Номер с панорамным видом на Неву, большой кроватью и рабочей зоной.',
   'https://storage.yandexcloud.net/your-bucket/room-spb-river.jpg'),
  (2, 'Семейный номер', 4, 9000.00,
   'Подходит для семьи, две комнаты, мини-кухня и детская кроватка по запросу.',
   'https://storage.yandexcloud.net/your-bucket/room-spb-family.jpg'),

-- Номера для отеля 3 (Сочи Резорт)
  (3, 'Номер у моря', 2, 8000.00,
   'Светлый номер с балконом и видом на море.',
   'https://storage.yandexcloud.net/your-bucket/room-sochi-sea.jpg'),
  (3, 'Люкс с террасой', 4, 12000.00,
   'Просторный люкс с террасой, зоной отдыха и отдельной спальней.',
   'https://storage.yandexcloud.net/your-bucket/room-sochi-lux.jpg');
