-- Create (if you don't already have it) and use it
CREATE DATABASE IF NOT EXISTS hotel_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hotel_system;

-- Ensure InnoDB for FK support
SET default_storage_engine=INNODB;
-- USERS
CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(80)  NOT NULL UNIQUE,
  email    VARCHAR(120) NOT NULL UNIQUE,
  password_hash CHAR(64) NOT NULL,              -- SHA-256 hex
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- HOTELS
CREATE TABLE IF NOT EXISTS hotels (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(120) NOT NULL,
  city VARCHAR(80) NOT NULL,
  country VARCHAR(60) NOT NULL,
  timezone VARCHAR(60) NOT NULL
) ENGINE=InnoDB;

-- ROOM TYPES
CREATE TABLE IF NOT EXISTS room_types (
  id INT AUTO_INCREMENT PRIMARY KEY,
  hotel_id INT NOT NULL,
  name VARCHAR(80) NOT NULL,
  max_guests INT NOT NULL,
  description TEXT,
  CONSTRAINT fk_roomtype_hotel
    FOREIGN KEY (hotel_id) REFERENCES hotels(id)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- RATE PLANS
CREATE TABLE IF NOT EXISTS rate_plans (
  id INT AUTO_INCREMENT PRIMARY KEY,
  room_type_id INT NOT NULL,
  name VARCHAR(80),
  currency CHAR(3),
  base_price DECIMAL(10,2),
  CONSTRAINT fk_rateplan_roomtype
    FOREIGN KEY (room_type_id) REFERENCES room_types(id)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- INVENTORY (one row per room_type per date)
CREATE TABLE IF NOT EXISTS inventory (
  id INT AUTO_INCREMENT PRIMARY KEY,
  room_type_id INT NOT NULL,
  `date` DATE NOT NULL,
  allotment INT NOT NULL,
  CONSTRAINT fk_inventory_roomtype
    FOREIGN KEY (room_type_id) REFERENCES room_types(id)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT uq_inventory UNIQUE (room_type_id, `date`)
) ENGINE=InnoDB;

-- PRICE OVERRIDES (per rate_plan per date)
CREATE TABLE IF NOT EXISTS price_overrides (
  id INT AUTO_INCREMENT PRIMARY KEY,
  rate_plan_id INT NOT NULL,
  `date` DATE NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  CONSTRAINT fk_priceoverride_rateplan
    FOREIGN KEY (rate_plan_id) REFERENCES rate_plans(id)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT uq_price_override UNIQUE (rate_plan_id, `date`)
) ENGINE=InnoDB;

-- BOOKINGS
CREATE TABLE IF NOT EXISTS bookings (
  id CHAR(36) PRIMARY KEY,               -- store a UUID from Java
  user_id INT NOT NULL,
  hotel_id INT NOT NULL,
  check_in DATE NOT NULL,
  check_out DATE NOT NULL,
  total DECIMAL(10,2),
  currency CHAR(3),
  status ENUM('PENDING','CONFIRMED','CANCELLED') NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_booking_user  FOREIGN KEY (user_id)  REFERENCES users(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_booking_hotel FOREIGN KEY (hotel_id) REFERENCES hotels(id)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- BOOKING ITEMS
CREATE TABLE IF NOT EXISTS booking_items (
  id INT AUTO_INCREMENT PRIMARY KEY,
  booking_id CHAR(36) NOT NULL,
  room_type_id INT NOT NULL,
  rate_plan_id INT NOT NULL,
  qty INT NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  CONSTRAINT fk_bi_booking   FOREIGN KEY (booking_id)  REFERENCES bookings(id)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_bi_roomtype  FOREIGN KEY (room_type_id) REFERENCES room_types(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_bi_rateplan  FOREIGN KEY (rate_plan_id) REFERENCES rate_plans(id)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Helpful indexes (speed up searches)
CREATE INDEX idx_room_types_hotel    ON room_types(hotel_id);
CREATE INDEX idx_rate_plans_roomtype ON rate_plans(room_type_id);
CREATE INDEX idx_inventory_date      ON inventory(room_type_id, `date`);
CREATE INDEX idx_price_date          ON price_overrides(rate_plan_id, `date`);
CREATE INDEX idx_bookings_user       ON bookings(user_id, created_at);
CREATE INDEX idx_bookings_hotel      ON bookings(hotel_id, created_at);
-- Demo user: username 'demo', password 'demo123'
INSERT INTO users (username, email, password_hash)
VALUES ('demo','demo@example.com', SHA2('demo123',256))
ON DUPLICATE KEY UPDATE email = VALUES(email);

-- One hotel + two room types + a rate plan
INSERT INTO hotels (name, city, country, timezone)
VALUES ('Ocean View Inn','San Diego','USA','America/Los_Angeles');

INSERT INTO room_types (hotel_id, name, max_guests, description)
VALUES 
  (1,'Queen Room',2,'Cozy city view'),
  (1,'King Suite',3,'Ocean-facing');

INSERT INTO rate_plans (room_type_id, name, currency, base_price)
VALUES 
  (1,'Flexible', 'USD', 159.00),
  (2,'Flexible', 'USD', 249.00);

-- Inventory for a week
INSERT INTO inventory (room_type_id, `date`, allotment) VALUES
(1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 5),
(1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 5),
(1, DATE_ADD(CURDATE(), INTERVAL 3 DAY), 5),
(2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 3),
(2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 3),
(2, DATE_ADD(CURDATE(), INTERVAL 3 DAY), 3);

-- Price override (e.g., weekend surge)
INSERT INTO price_overrides (rate_plan_id, `date`, price)
VALUES (2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 299.00);
