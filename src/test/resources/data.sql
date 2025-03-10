DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS users;


CREATE TABLE users
(
    id           BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email        VARCHAR(255) NOT NULL,
    enabled      BOOLEAN      NOT NULL,
    first_name   VARCHAR(255) NOT NULL,
    last_name    VARCHAR(255) NOT NULL,
    password     VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) DEFAULT NULL,
    CONSTRAINT UK_email UNIQUE (email)
);

CREATE TABLE user_roles
(
    user_id BIGINT NOT NULL,
    roles   VARCHAR(255) DEFAULT NULL,
    CONSTRAINT roles_check CHECK (roles IN ('ROLE_USER', 'ROLE_ADMIN', 'ROLE_EMPLOYEE')),
    CONSTRAINT FK_user_roles FOREIGN KEY (user_id) REFERENCES users (id)
);
CREATE TABLE IF NOT EXISTS hotels
(
    id
    BIGINT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    active
    BOOLEAN
    NOT
    NULL,
    address
    VARCHAR
(
    255
) NOT NULL,
    base_price DECIMAL
(
    38,
    2
) NOT NULL,
    city VARCHAR
(
    255
) NOT NULL,
    contact_email VARCHAR
(
    255
),
    contact_phone VARCHAR
(
    255
),
    country VARCHAR
(
    255
) NOT NULL,
    description TEXT,
    name VARCHAR
(
    255
) NOT NULL,
    star_rating INT,
    total_rooms INT NOT NULL
    );

-- Create the bookings table in H2
CREATE TABLE IF NOT EXISTS bookings
(
    id
    BIGINT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    check_in_date
    DATE
    NOT
    NULL,
    check_out_date
    DATE
    NOT
    NULL,
    created_at
    TIMESTAMP
(
    6
) NOT NULL,
    number_of_guests INT NOT NULL,
    special_requests VARCHAR
(
    255
),
    status VARCHAR
(
    255
) NOT NULL CHECK
(
    status
    IN
(
    'PENDING',
    'CONFIRMED',
    'CANCELLED',
    'COMPLETED',
    'REFUNDED'
)),
    total_price DECIMAL
(
    38,
    2
) NOT NULL,
    updated_at TIMESTAMP
(
    6
),
    hotel_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT FK_hotel FOREIGN KEY
(
    hotel_id
) REFERENCES hotels
(
    id
),
    CONSTRAINT FK_user FOREIGN KEY
(
    user_id
) REFERENCES users
(
    id
)
    );


INSERT INTO users (id, email, enabled, first_name, last_name, password, phone_number)
VALUES (1, 'john.doe@example.com', TRUE, 'John', 'Doe', 'password123', NULL),
       (2, 'jane.smith@example.com', TRUE, 'Jane', 'Smith', '123456', NULL),
       (3, 'mike.johnson@example.com', TRUE, 'Mike', 'Johnson', 'mypassword', NULL),
       (4, 'susan.williams@example.com', TRUE, 'Susan', 'Williams', 'hello123', NULL);
INSERT INTO hotels (active, address, base_price, city, contact_email, contact_phone, country, description, name,
                    star_rating, total_rooms)
VALUES (TRUE, '123 Main St, Downtown', 150.00, 'Paris', 'contact@hotelparis.com', '123-456-7890', 'France',
        'A luxurious hotel in the heart of Paris', 'Hotel Paris', 5, 100),
       (TRUE, '456 Oak Ave, Uptown', 100.00, 'New York', 'info@hotelny.com', '987-654-3210', 'USA',
        'A modern hotel with beautiful views of New York City', 'Hotel New York', 4, 120),
       (TRUE, '789 Pine Rd, Seaside', 200.00, 'Sydney', 'support@hotelsydney.com', '555-123-4567', 'Australia',
        'A beachfront hotel with stunning ocean views', 'Hotel Sydney', 5, 80),
       (TRUE, '321 Birch Ln, Suburbia', 80.00, 'London', 'reservations@hotellondon.com', '333-444-5555', 'UK',
        'A cozy hotel located in a quiet suburban area', 'Hotel London', 3, 60);

INSERT INTO bookings (check_in_date, check_out_date, created_at, number_of_guests, special_requests, status,
                      total_price, updated_at, hotel_id, user_id)
VALUES ('2025-03-10', '2025-03-12', '2025-03-01 10:00:00', 2, 'No special requests', 'PENDING', 200.00, NULL, 1, 1),
       ('2025-03-15', '2025-03-18', '2025-03-02 11:00:00', 3, 'Late check-in requested', 'CONFIRMED', 450.00,
        '2025-03-05 14:00:00', 2, 2),
       ('2025-03-20', '2025-03-22', '2025-03-03 09:00:00', 1, NULL, 'CANCELLED', 100.00, NULL, 3, 3);