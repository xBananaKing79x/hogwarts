-- Создание таблицы car (машина)
CREATE TABLE car (
    id BIGSERIAL PRIMARY KEY,           -- Первичный ключ
    brand VARCHAR(100) NOT NULL,        -- Марка машины
    model VARCHAR(100) NOT NULL,        -- Модель машины
    price DECIMAL(10, 2) NOT NULL       -- Стоимость машины
);

-- Создание таблицы person (человек)
CREATE TABLE person (
    id BIGSERIAL PRIMARY KEY,           -- Первичный ключ
    name VARCHAR(100) NOT NULL,         -- Имя человека
    age INTEGER NOT NULL,               -- Возраст человека
    has_license BOOLEAN,                -- Признак наличия прав (может быть NULL)
    car_id BIGINT NOT NULL REFERENCES car(id)  -- Внешний ключ на таблицу car (обязательный)
);
