DO $$ BEGIN
    CREATE TYPE sex AS ENUM ('парень', 'девушка', 'нет ответа');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE psychotype AS ENUM ('интроверт', 'экстраверт', '50 на 50', 'нет ответа');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE alcohol AS ENUM ('за компанию', 'никогда', 'часто', 'воздерживаюсь', 'нет ответа');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE smoking AS ENUM ('нет', 'бросаю', 'часто', 'редко', 'нет ответа');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE msg_status AS ENUM ('просмотрено', 'доставлено', 'не отправлено', 'хз');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE period_of_time AS ENUM ('утро', 'день', 'вечер', 'ночь');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE partner AS ENUM ('парень', 'девушка', 'компания', 'любой');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE roles AS ENUM ('admin', 'user', 'unknown', 'moderator');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE add_status AS ENUM ('в ожидании', 'добавлен', 'отклонен', 'удален', 'хз');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     email VARCHAR(64) NOT NULL UNIQUE,
                                     phone VARCHAR(14),
                                     nickname VARCHAR(64) NOT NULL,
                                     date_of_birthday DATE,
                                     e_sex sex NOT NULL,
                                     about_me VARCHAR(255),
                                     e_smoking smoking,
                                     e_alcohol alcohol,
                                     e_psychotype psychotype,
                                     url_avatar VARCHAR(64),
                                     e_role roles DEFAULT 'unknown',
                                     sol VARCHAR(255),
                                     password VARCHAR(255),
                                     created_at TIMESTAMP,
                                     updated_at TIMESTAMP,
                                     city VARCHAR(255)
);