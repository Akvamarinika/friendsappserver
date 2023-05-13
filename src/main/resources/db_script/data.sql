INSERT INTO countries (name) VALUES
    ('Россия');

INSERT INTO federal_districts (name, country_id) VALUES
                                         ('Сибирский', 1),
                                         ('Приволжский', 1),
                                         ('Южный', 1);

INSERT INTO regions (name, federal_district_id) VALUES
                                                    ('Хакасия', 2),
                                                    ('Тыва', 2),
                                                    ('Красноярский край', 1),
                                                    ('Иркутская область', 1);

INSERT INTO cities (lat, lon, name, country_id, region_id) VALUES
                                             (52.29, 104.3, 'Иркутск', 1, 4),
                                             (123.123, 145.236, 'Красноярск', 1, 3),
                                             (null, null, 'Ангарск', 1, 4);

INSERT INTO event_categories (name) VALUES
                                          ('Бары, кафе и рестораны'),
                                          ('Прогулки'),
                                          ('Спорт'),
                                          ('Кино, театры'),
                                          ('Путешествия'),
                                          ('Концерты'),
                                          ('Походы'),
                                          ('Фестивали'),
                                          ('Хакатоны'),
                                          ('Другое');

INSERT INTO users (username, email, phone, password, nickname, date_of_birthday, e_sex, about_me, e_smoking, e_alcohol, e_psychotype, url_avatar, city_id, created_at, updated_at, vk_id, enabled)
VALUES
    ('1234567', 'victor@example.com', '89895551234', '$2a$10$eNkLTyFdWKntk1gBO4pp7OiGmm/TaI7Y.8mJVKv.xk8bQGvauQ.8S', 'Виктор', '2000-01-01', 1, 'Привет! Мое имя Виктор.', 0, 0, 1, 'https://meragor.com/files/styles//ava_800_800_wm/sfztn_boy_avatar_1.jpg', 1, NOW(), NOW(), '1234567', true),
    ('anna@example.com', 'anna@example.com', '89505552345', '$2a$10$eNkLTyFdWKntk1gBO4pp7OiGmm/TaI7Y.8mJVKv.xk8bQGvauQ.8S', 'Анна', '1998-05-05', 2, 'Привет! Мое имя Анна.', 1, 0, 2, 'https://meragor.com/files/styles//ava_800_800_wm/_5_5.jpg', 2, NOW(), NOW(), NULL, true),
    ('peter@example.com', 'peter@example.com', '89145553456', '$2a$10$eNkLTyFdWKntk1gBO4pp7OiGmm/TaI7Y.8mJVKv.xk8bQGvauQ.8S', 'Пётр', '1995-10-10', 1, 'Привет! Мое имя Пётр.', 1, 3, 2, 'https://demotivation.ru/wp-content/uploads/2020/11/905e85b5e1f2b5e1935c81b3c2478829.jpg', 1, NOW(), NOW(), NULL, true),
    ('mary@example.com', 'mary@example.com', '89245554567', '$2a$10$eNkLTyFdWKntk1gBO4pp7OiGmm/TaI7Y.8mJVKv.xk8bQGvauQ.8S', 'Мария', '2005-12-25', 2, 'Привет! Мое имя Мария.', 2, 2, 0, 'https://meragor.com/files/styles//ava_800_800_wm/avatar-211226-001768.png', 3,  NOW(), NOW(), NULL, true),
    ('alex@example.com', 'alex@example.com', '89505555678', '$2a$10$eNkLTyFdWKntk1gBO4pp7OiGmm/TaI7Y.8mJVKv.xk8bQGvauQ.8S', 'Александр', '2003-02-14', 1, 'Привет! Мое имя Александр.', 3, 3, 0, 'https://meragor.com/files/styles//ava_800_800_wm/sfztn_boy_avatar_18.jpg', 2,  NOW(), NOW(), NULL, true),
    ('victoria@example.com', 'victoria@example.com', NULL, '$2a$10$eNkLTyFdWKntk1gBO4pp7OiGmm/TaI7Y.8mJVKv.xk8bQGvauQ.8S', 'Вика', '1998-07-20', 2, 'Привет! Мое имя Виктория.', 4, 4, 2, 'https://meragor.com/files/styles//ava_800_800_wm/avatar-210866-000320.png', 3,  NOW(), NOW(), NULL, true),
    ('nikita@example.com', 'nikita@example.com', NULL, '$2a$10$eNkLTyFdWKntk1gBO4pp7OiGmm/TaI7Y.8mJVKv.xk8bQGvauQ.8S', 'Никита', '1997-03-17', 1, 'Привет! Мое имя Никита.', 1, 3, 3, 'https://it-doc.info/wp-content/uploads/2019/06/avatar-9.jpg', 1,  NOW(), NOW(), NULL, true),
    ('nick@example.com', 'nick@example.com', NULL, '$2a$10$eNkLTyFdWKntk1gBO4pp7OiGmm/TaI7Y.8mJVKv.xk8bQGvauQ.8S', 'Николай', '1999-11-11', 1, 'Привет! Мое имя Николай.', 2, 2, 3, 'https://meragor.com/files/styles//ava_800_800_wm/sfztn_boy_avatar_64.jpg', 2,  NOW(), NOW(), NULL, true),
    ('david@example.com', 'david@example.com', NULL, '$2a$10$eNkLTyFdWKntk1gBO4pp7OiGmm/TaI7Y.8mJVKv.xk8bQGvauQ.8S', 'Давид', '1999-09-15', 1, 'Привет! Мое имя Давид.', 3, 1, 2, 'https://it-doc.info/wp-content/uploads/2019/06/avatar-guitar-man.jpg', 3,  NOW(), NOW(), NULL, true),
    ('7654321', 'inna@example.com', NULL, '$2a$10$eNkLTyFdWKntk1gBO4pp7OiGmm/TaI7Y.8mJVKv.xk8bQGvauQ.8S', 'Инна', '2002-06-30', 2, 'Привет! Мое имя Инна.', 4, 0, 1, 'https://meragor.com/files/styles//ava_800_800_wm/2_8.jpg', 3,  NOW(), NOW(), '7654321', true);


INSERT INTO events (name, description, date, e_period_of_time, e_partner, created_at, updated_at, event_category_id, owner_id) VALUES
                                               ('Фестиваль Байкальской лиги КВН', 'Ищу единомышленника, чтобы посетить фестиваль КВН.', '2023-04-11', 1, 1, NOW(), NOW(), 8, 1),
                                               ('ХХХ Всероссийский фестиваль «Российская студенческая весна»', 'Хочу с кем-нибудь пойти на студенческую весну. Присоединяйтесь, будет весело!', '2023-04-20', 2, 4, NOW(), NOW(), 8, 6),
                                               ('Эко-хакатон "Полюс золото"', 'Набираю группу для участия в хакатоне. Кому интересно, пишите в комменты!', '2023-05-03', 3, 3, NOW(), NOW(), 3, 2),
                                               ('Отчетный концерт танцевального центра "ПРО танцы"', 'Хочу пойти компанией на концерт.', '2023-06-06', 4, 3, NOW(), NOW(), 6, 7),
                                               ('Праздичная программа, посвященная Дню молодежи', 'Ищу компанию, чтобы сходить на концерт.', '2023-06-26', 2, 3, NOW(), NOW(), 6, 3),
                                               ('РОК-Фестиваль', 'Кто хочет тоже пойти на рок-концерт, присоединяйтесь!', '2023-04-14', 4, 4, NOW(), NOW(), 8, 9),
                                               ('Фестиваль песни на английском языке', 'Неделя английского языка, Фестиваль песни на английском языке', '2023-05-18', 1, 2, NOW(), NOW(), 8, 4),
                                               ('Спектакль Народного театра «Предместье» «Старший сын»', 'С кем-нибудь пойду на спектакль..', '2023-06-09', 2, 4, NOW(), NOW(), 8, 8),
                                               ('Международный фестиваль "Барабаны мира"', 'GO на фестиваль!', '2023-06-20', 3, 2, NOW(), NOW(), 4, 5),
                                               ('Концертная программа танцевального центра «ARBO»', 'Желающие пойти на концерт, отзовитесь!', '2023-06-10', 1, 4, NOW(), NOW(), 6, 2);

INSERT INTO user_authorities (user_id, authorities) VALUES
                                                        (1, 'USER'),
                                                        (2, 'USER'),
                                                        (3, 'USER'),
                                                        (4, 'USER'),
                                                        (5, 'USER'),
                                                        (6, 'USER'),
                                                        (7, 'USER'),
                                                        (8, 'USER'),
                                                        (9, 'USER'),
                                                        (10, 'USER');

INSERT INTO comments (user_id, event_id, text, created_at, updated_at, edited)
VALUES
    (1, 2, 'Это отличное событие!', NOW(), NOW(), false),
    (2, 2, 'Мне было так весело!', NOW(), NOW(), false),
    (3, 1, 'Это событие могло быть лучше', NOW(), NOW(), false),
    (1, 3, 'С нетерпением жду следующего!', NOW(), NOW(), false),
    (4, 4, 'Это был мой первый визит, и я был впечатлен!', NOW(), NOW(), false),
    (2, 3, 'Я думаю, что место было идеальным', NOW(), NOW(), false),
    (5, 1, 'Еда была восхитительной!', NOW(), NOW(), false),
    (1, 5, 'Музыка была слишком громкой', NOW(), NOW(), false),
    (3, 4, 'Мне понравилось оформление!', NOW(), NOW(), false),
    (2, 1, 'Хотелось бы, чтобы было больше активности', NOW(), NOW(), false),
    (1, 6, 'Организаторы отлично поработали', NOW(), NOW(), false),
    (4, 3, 'Мне было весело!', NOW(), NOW(), false),
    (3, 2, 'Я встретил так много новых людей', NOW(), NOW(), false),
    (1, 4, 'Мероприятие было хорошо организовано', NOW(), NOW(), false),
    (5, 5, 'Мне показалось, что событие слишком длинное', NOW(), NOW(), false),
    (2, 6, 'Мне понравились фото с мероприятия!', NOW(), NOW(), false),
    (3, 5, 'Думаю, на мероприятии могло быть больше сидячих мест', NOW(), NOW(), false),
    (1, 1, 'Мероприятие было слишком многолюдным, на мой вкус', NOW(), NOW(), false),
    (4, 2, 'Я прекрасно провел время!', NOW(), NOW(), false),
    (5, 6, 'Напитки были восхитительны', NOW(), NOW(), false);

INSERT INTO events_and_participants (event_id, user_id, feedback_type, created_at, updated_at, owner_viewed, participant_viewed)
VALUES
    (1, 2, 'APPROVED', '2023-05-15 10:00:00', '2023-05-15 10:00:00', false, false),
    (1, 3, 'REJECTED', '2023-05-15 10:00:00', '2023-05-15 10:00:00', false, false),
    (1, 4, 'WAITING', '2023-05-15 10:00:00', '2023-05-15 10:00:00', false, false),
    (2, 1, 'APPROVED', '2023-05-15 10:00:00', '2023-05-15 10:00:00', false, false),
    (2, 2, 'REJECTED', '2023-05-15 10:00:00', '2023-05-15 10:00:00', false, true),
    (2, 3, 'WAITING', '2023-05-15 10:00:00', '2023-05-15 10:00:00', false, false),
    (2, 4, 'WAITING', '2023-05-15 10:00:00', '2023-05-15 10:00:00', false, false),
    (3, 1, 'APPROVED', '2023-05-15 10:00:00', '2023-05-15 10:00:00', true, false),
    (3, 3, 'REJECTED', '2023-05-15 10:00:00', '2023-05-15 10:00:00', false, false),
    (3, 4, 'WAITING', '2023-05-15 10:00:00', '2023-05-15 10:00:00', false, false),
    (3, 5, 'APPROVED', '2023-05-15 10:00:00', '2023-05-15 10:00:00', false, false),
    (7, 2, 'WAITING', '2023-05-15 10:00:00', '2023-05-10 12:00:00', false, false),
    (8, 2, 'WAITING', '2023-05-15 10:00:00', '2023-05-15 10:00:00', false, false);
