--TRUNCATE users;

INSERT INTO users (about_me,
                   e_alcohol,
                   created_at,
                   date_of_birthday,
                   email,
                   nickname,
                   password,
                   phone,
                   e_psychotype,
                  -- e_role,
                   e_sex,
                   e_smoking,
                   updated_at,
                   url_avatar,
                   vk_id,
                   city_id,
                   enabled) VALUES (
                                       null,
                                       null,
                                       '2023-03-14 18:25:19.904272',
                                       '1995-03-03',
                                       'akvamarin@gmail.com',
                                       'Akvamarin',
                                       '$2a$10$ZmJxIt7HaqxXqwpvd7scte0UmLndHSn2DgZVS99Ug0xZRck2rJ6fO',
                                       '89991210000',
                                       null,
                                       --null,
                                       null,
                                       null,
                                       '2023-03-14 18:25:19.904272',
                                       'http://********',
                                       null,
                                       null,
                                    true),
                                   (
                                    'Люблю отдыхать на природе',
                                    0,
                                    '2023-03-14 18:25:19.904272',
                                    '2000-03-03',
                                    'kate2000@gmail.com',
                                    'Kate',
                                    '$2a$10$ZmJxIt7HaqxXqwpvd7scte0UmLndHSn2DgZVS99Ug0xZRck2rJ6fO',
                                    '89991213333',
                                    0,
                                    --0,
                                    0,
                                    0,
                                    '2023-03-14 18:25:19.904272',
                                    'http://********',
                                    'vk111',
                                    null,
                                    true),

                                   ('Мои хобби: автомобили, ходить в походы, коллекционирую редкие дорогие вещи',
                                    0,
                                    '2023-03-13 18:25:19.904272',
                                    '1998-02-07',
                                    'alex2000@mail.ru',
                                    'Alex',
                                    '$2a$10$ZmJxIt7HaqxXqwpvd7scte0UmLndHSn2DgZVS99Ug0xZRck2rJ6fO',
                                    '89501112323',
                                    0,
                                    --0,
                                    0,
                                    0,
                                    '2023-03-13 18:25:19.904272',
                                    'http://********',
                                    'vkvkvk',
                                    null,
                                    true);

