DELETE FROM meals;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (datetime, description, calories, user_id) VALUES
  ('2018-03-20 11:00', 'пельмешки', 535, 100000),
  ('2018-03-21 13:00', 'sushi', 407, 100000),
  ('2018-03-21 20:00', 'spaghetti', 321, 100000),
  ('2018-03-20 13:30', 'burger', 378, 100001),
  ('2018-03-21 11:00', 'potatoes', 412, 100001),
  ('2018-03-21 13:00', 'перекус', 216, 100001),
  ('2018-03-21 20:00', 'yogurt', 260, 100001);

