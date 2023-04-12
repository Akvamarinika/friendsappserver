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
                                             (123.123, 145.236, 'Иркутск', 1, 4),
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
                                          ('Другое');

INSERT INTO users (username, email, phone, password, nickname, date_of_birthday, e_sex, about_me, e_smoking, e_alcohol, e_psychotype, url_avatar, city_id, created_at, updated_at, vk_id, enabled)
VALUES
    ('1234567', 'john@example.com', '89895551234', 'password1', 'John', '1990-01-01', 1, 'Hi, I am John.', 0, 0, 1, 'https://meragor.com/files/styles//ava_800_800_wm/sfztn_boy_avatar_1.jpg', 1, NOW(), NOW(), '1234567', true),
    ('jane@example.com', 'jane@example.com', '89505552345', 'password2', 'Jane', '1995-05-05', 2, 'Hi, I am Jane.', 1, 0, 2, 'https://meragor.com/files/styles//ava_800_800_wm/_5_5.jpg', 2, NOW(), NOW(), NULL, true),
    ('peter@example.com', 'peter@example.com', '89145553456', 'password3', 'Peter', '1985-10-10', 1, 'Hi, I am Peter.', 1, 3, 2, 'https://demotivation.ru/wp-content/uploads/2020/11/905e85b5e1f2b5e1935c81b3c2478829.jpg', 1, NOW(), NOW(), NULL, true),
    ('mary@example.com', 'mary@example.com', '89245554567', 'password4', 'Mary', '1980-12-25', 2, 'Hi, I am Mary.', 2, 2, 0, 'https://meragor.com/files/styles//ava_800_800_wm/avatar-211226-001768.png', 3,  NOW(), NOW(), NULL, true),
    ('tom@example.com', 'tom@example.com', '89505555678', 'password5', 'Tom', '2000-02-14', 1, 'Hi, I am Tom.', 3, 3, 0, 'https://meragor.com/files/styles//ava_800_800_wm/sfztn_boy_avatar_18.jpg', 2,  NOW(), NOW(), NULL, true),
    ('jenny@example.com', 'jenny@example.com', NULL, 'password6', 'Jenny', '1998-07-20', 2, 'Hi, I am Jenny.', 4, 4, 2, 'https://meragor.com/files/styles//ava_800_800_wm/avatar-210866-000320.png', 3,  NOW(), NOW(), NULL, true),
    ('ted@example.com', 'ted@example.com', NULL, 'password7', 'Ted', '1992-03-17', 1, 'Hi, I am Ted.', 1, 3, 3, 'https://it-doc.info/wp-content/uploads/2019/06/avatar-9.jpg', 1,  NOW(), NOW(), NULL, true),
    ('nick@example.com', 'nick@example.com', NULL, 'password8', 'Nick', '1999-11-11', 1, 'Hi, I am Nick.', 2, 2, 3, 'https://meragor.com/files/styles//ava_800_800_wm/sfztn_boy_avatar_64.jpg', 2,  NOW(), NOW(), NULL, true),
    ('david@example.com', 'david@example.com', NULL, 'password9', 'David', '1996-09-15', 1, 'Hi, I am David.', 3, 1, 2, 'https://it-doc.info/wp-content/uploads/2019/06/avatar-guitar-man.jpg', 3,  NOW(), NOW(), NULL, true),
    ('7654321', 'amy@example.com', NULL, 'password10', 'Amy', '1988-06-30', 2, 'Hi, I am Amy.', 4, 0, 1, 'https://meragor.com/files/styles//ava_800_800_wm/2_8.jpg', 3,  NOW(), NOW(), '7654321', true);


INSERT INTO events (name, description, date, e_period_of_time, e_partner, created_at, updated_at, event_category_id, owner_id) VALUES
                                               ('Event 1', 'Description for Event 1', '2023-05-01', 1, 1, NOW(), NOW(), 1, 1),
                                               ('Event 2', 'Description for Event 2', '2023-04-02', 2, 4, NOW(), NOW(), 2, 1),
                                               ('Event 3', 'Description for Event 3', '2023-04-03', 3, 3, NOW(), NOW(), 3, 2),
                                               ('Event 4', 'Description for Event 4', '2023-05-04', 4, 1, NOW(), NOW(), 4, 2),
                                               ('Event 5', 'Description for Event 5', '2023-04-05', 2, 3, NOW(), NOW(), 5, 3),
                                               ('Event 6', 'Description for Event 6', '2023-06-06', 4, 4, NOW(), NOW(), 6, 3),
                                               ('Event 7', 'Description for Event 7', '2023-04-07', 1, 2, NOW(), NOW(), 7, 4),
                                               ('Event 8', 'Description for Event 8', '2023-06-08', 2, 4, NOW(), NOW(), 8, 4),
                                               ('Event 9', 'Description for Event 9', '2023-04-09', 3, 2, NOW(), NOW(), 9, 5),
                                               ('Event 10', 'Description for Event 10', '2023-05-10', 1, 4, NOW(), NOW(), 3, 5);

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

INSERT INTO comments (user_id, event_id, text, created_at, updated_at)
VALUES
    (1, 2, 'This is a great event!', NOW(), NOW()),
    (2, 2, 'I had so much fun!', NOW(), NOW()),
    (3, 1, 'This event could have been better', NOW(), NOW()),
    (1, 3, 'Looking forward to the next one!', NOW(), NOW()),
    (4, 4, 'This was my first time attending, and I was impressed!', NOW(), NOW()),
    (2, 3, 'I think the location was perfect', NOW(), NOW()),
    (5, 1, 'The food was amazing!', NOW(), NOW()),
    (1, 5, 'The music was too loud', NOW(), NOW()),
    (3, 4, 'I loved the decorations!', NOW(), NOW()),
    (2, 1, 'I wish there were more activities to do', NOW(), NOW()),
    (1, 6, 'The organizers did a great job', NOW(), NOW()),
    (4, 3, 'I had a blast!', NOW(), NOW()),
    (3, 2, 'I met so many new people', NOW(), NOW()),
    (1, 4, 'The event was well-organized', NOW(), NOW()),
    (5, 5, 'I thought the event was too long', NOW(), NOW()),
    (2, 6, 'I loved the photo booth!', NOW(), NOW()),
    (3, 5, 'I think the event could have used more seating', NOW(), NOW()),
    (1, 1, 'The event was a bit too crowded for my taste', NOW(), NOW()),
    (4, 2, 'I had a great time!', NOW(), NOW()),
    (5, 6, 'The drinks were delicious', NOW(), NOW());






