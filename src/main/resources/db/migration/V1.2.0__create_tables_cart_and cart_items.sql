CREATE TABLE IF NOT EXISTS carts (
    id SERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT unique_user_cart UNIQUE (user_id)
);

CREATE TABLE IF NOT EXISTS cart_items (
    id SERIAL PRIMARY KEY,
    cart_id BIGINT REFERENCES carts(id) ON DELETE CASCADE,
    product_id BIGINT REFERENCES products(id),
    quantity INTEGER NOT NULL
);

-- Insert some sample products
INSERT INTO products (code, name, description, image, category, price, quantity, internal_reference, shell_id, inventory_status, rating, created_at, updated_at)
VALUES
    ('prod-001', 'Smartphone X', 'Dernier smartphone avec fonctionnalités avancées', 'smartphone-x.jpg', 'Électronique', 749000.0, 50, 'INT-001', 1001, 'INSTOCK', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('prod-002', 'Laptop Pro', 'Ordinateur portable haute performance pour professionnels', 'laptop-pro.jpg', 'Électronique', 129999.0, 30, 'INT-002', 1002, 'INSTOCK', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('prod-003', 'Écouteurs sans fil', 'Écouteurs sans fil à réduction de bruit', 'headphones.jpg', 'Électronique', 100000.0, 100, 'INT-003', 1003, 'INSTOCK', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('prod-004', 'Montre connectée', 'Montre connectée avec suivi de fitness', 'smartwatch.jpg', 'Électronique', 249705.99, 20, 'INT-004', 1004, 'LOWSTOCK', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('prod-005', 'Enceinte Bluetooth', 'Enceinte bluetooth portable avec une excellente qualité sonore', 'speaker.jpg', 'Électronique', 85000.0, 0, 'INT-005', 1005, 'OUTOFSTOCK', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
    ON CONFLICT (code) DO NOTHING;