CREATE TABLE IF NOT EXISTS wishlists (
    id SERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT unique_user_wishlist UNIQUE (user_id)
    );

CREATE TABLE IF NOT EXISTS wishlist_products (
    wishlist_id BIGINT REFERENCES wishlists(id) ON DELETE CASCADE,
    product_id BIGINT REFERENCES products(id) ON DELETE CASCADE,
    PRIMARY KEY (wishlist_id, product_id)
    );

-- Ajout d'un index pour améliorer les performances des recherches
CREATE INDEX idx_wishlist_products_product_id ON wishlist_products(product_id);