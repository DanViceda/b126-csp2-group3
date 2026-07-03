CREATE DATABASE IF NOT EXISTS tesda_project;
USE tesda_project;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(11) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS pets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pet_name VARCHAR(100) NOT NULL,
    species VARCHAR(100) NOT NULL,
    breed VARCHAR(100) NOT NULL,
    gender VARCHAR(50) NOT NULL,
    age INT NOT NULL,
    weight DOUBLE NOT NULL,
    user_id INT NOT NULL,
    CONSTRAINT fk_pets_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS food (
    id INT AUTO_INCREMENT PRIMARY KEY,
    food_name VARCHAR(100) NOT NULL,
    brand VARCHAR(100) NOT NULL,
    `type` VARCHAR(100) NOT NULL,
    flavor VARCHAR(100) NOT NULL,
    expiration_date DATE NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS inventory (
    id INT AUTO_INCREMENT PRIMARY KEY,
    food_id INT NOT NULL,
    quantity_available DOUBLE NOT NULL,
    unit VARCHAR(50) NOT NULL,
    last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_inventory_food
        FOREIGN KEY (food_id) REFERENCES food(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS schedule (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pet_id INT NOT NULL,
    food_id INT NOT NULL,
    feeding_time VARCHAR(20) NOT NULL,
    quantity DOUBLE NOT NULL,
    frequency VARCHAR(100) NOT NULL,
    CONSTRAINT fk_schedule_pet
        FOREIGN KEY (pet_id) REFERENCES pets(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_schedule_food
        FOREIGN KEY (food_id) REFERENCES food(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS feeding_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    schedule_id INT NOT NULL,
    feeding_date DATE NOT NULL,
    feeding_time VARCHAR(20) NOT NULL,
    status VARCHAR(100) NOT NULL,
    remarks VARCHAR(255) NOT NULL,
    CONSTRAINT fk_history_schedule
        FOREIGN KEY (schedule_id) REFERENCES schedule(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;
