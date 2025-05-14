CREATE TABLE IF NOT EXISTS products (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    image VARCHAR(255),
    category VARCHAR(100),
    price NUMERIC(10, 2) NOT NULL,
    quantity INTEGER NOT NULL,
    internal_reference VARCHAR(100),
    shell_id BIGINT,
    inventory_status VARCHAR(20) CHECK (inventory_status IN ('INSTOCK', 'LOWSTOCK', 'OUTOFSTOCK')),
    rating INTEGER,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    firstname VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
    );

-- Insert admin user with hashed password 'admin123'
INSERT INTO users (username, firstname, email, password, created_at, updated_at)
VALUES ('admin', 'Admin', 'admin@admin.com', '$2a$10$d.vAIF./.UPmhoYh5OQyVO0UkpGCFto6jJShY/WdloWfSrDkEsK2O', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
    ON CONFLICT (email) DO NOTHING;

