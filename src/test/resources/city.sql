--TRUNCATE cities;
--TRUNCATE countries;
--TRUNCATE regions;
--TRUNCATE federal_districts;

INSERT INTO federal_districts (name) VALUES
('Сибирский'),
('Приволжский'),
('Южный');

INSERT INTO regions (name, federal_district_id) VALUES
('Хакасия', 1),
('Тыва', 1),
('Алтайский край', 1);

INSERT INTO countries (name) VALUES
('Россия');

INSERT INTO cities (lat, lon, name, country_id, region_id) VALUES (1111111, 22222, 'Иркутск', 1, 3)