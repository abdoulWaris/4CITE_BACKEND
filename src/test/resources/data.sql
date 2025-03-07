DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                       email VARCHAR(255) NOT NULL,
                       enabled BOOLEAN NOT NULL,
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       phone_number VARCHAR(255) DEFAULT NULL,
                       CONSTRAINT UK_email UNIQUE (email)
);

CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            roles VARCHAR(255) DEFAULT NULL,
                            CONSTRAINT roles_check CHECK (roles IN ('ROLE_USER', 'ROLE_ADMIN', 'ROLE_EMPLOYEE')),
                            CONSTRAINT FK_user_roles FOREIGN KEY (user_id) REFERENCES users(id)
);


INSERT INTO users (id, email, enabled, first_name, last_name, password, phone_number)
VALUES
    (1, 'john.doe@example.com', TRUE, 'John', 'Doe', 'password123', NULL),
    (2, 'jane.smith@example.com', TRUE, 'Jane', 'Smith', '123456', NULL),
    (3, 'mike.johnson@example.com', TRUE, 'Mike', 'Johnson', 'mypassword', NULL),
    (4, 'susan.williams@example.com', TRUE, 'Susan', 'Williams', 'hello123', NULL);
