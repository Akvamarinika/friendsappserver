DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS cities;
DROP TABLE IF EXISTS regions;
DROP TABLE IF EXISTS federal_districts;
DROP TABLE IF EXISTS countries;


create table IF NOT EXISTS countries
(
    id   bigint generated by default as identity
        constraint countries_pkey
            primary key,
    name varchar(255)
);

alter table countries
    owner to postgres;

create table IF NOT EXISTS federal_districts
(
    id   bigint generated by default as identity
        constraint federal_districts_pkey
            primary key,
    name varchar(255)
);

alter table  federal_districts
    owner to postgres;

create table IF NOT EXISTS regions
(
    id                  bigint generated by default as identity
        constraint regions_pkey
            primary key,
    name                varchar(255),
    federal_district_id bigint
        constraint fkh7piodpd566k9v37c6q8qhn80
            references federal_districts
);

alter table regions
    owner to postgres;

create table IF NOT EXISTS cities
(
    id         bigint generated by default as identity
        constraint cities_pkey
            primary key,
    lat        double precision not null,
    lon        double precision not null,
    name       varchar(255),
    country_id bigint
        constraint fk6gatmv9dwedve82icy8wrkdmk
            references countries,
    region_id  bigint
        constraint fk312nt0rik1hqh54qg34l4nsk3
            references regions
);

alter table cities
    owner to postgres;

create table IF NOT EXISTS users
(
    id               bigint generated by default as identity
        constraint users_pkey
            primary key,
    about_me         varchar(320),
    e_alcohol        integer,
    created_at       timestamp    not null,
    date_of_birthday date         not null,
    email            varchar(64)  not null
        constraint uk_6dotkott2kjsp8vw4d0m25fb7
            unique,
    nickname         varchar(50)  not null,
    password         varchar(128) not null,
    phone            varchar(14)  not null
        constraint uk_du5v5sr43g5bfnji4vb8hg5s3
            unique,
    e_psychotype     integer,
    e_role           integer,
    e_sex            integer,
    e_smoking        integer,
    updated_at       timestamp    not null,
    url_avatar       varchar(255),
    vk_id            varchar(64),
    city_id          bigint
        constraint fkn36jwt4acj3il2ixvv2c0ncco
            references cities
);

alter table users
    owner to postgres;