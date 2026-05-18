CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(100),
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    stock INTEGER NOT NULL DEFAULT 0 CHECK (stock >= 0),
    image_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_products_category ON products(category);
CREATE INDEX IF NOT EXISTS idx_products_price ON products(price);

COMMENT ON TABLE products IS 'Table containing all product information';
COMMENT ON COLUMN products.id IS 'unique id for each product';
COMMENT ON COLUMN products.name IS 'Product display name';
COMMENT ON COLUMN products.description IS 'Product full description';
COMMENT ON COLUMN products.category IS 'Product category';
COMMENT ON COLUMN products.price IS 'Product price';
COMMENT ON COLUMN products.stock IS 'Product availability';
COMMENT ON COLUMN products.image_url IS 'Product image on the cloud';
COMMENT ON COLUMN products.created_at IS 'Product creation time';
COMMENT ON COLUMN products.updated_at IS 'Time stamp after updating product';