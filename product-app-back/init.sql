CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price NUMERIC(10, 2) NOT NULL,
    expiration_date DATE
);

INSERT INTO product (name, description, price, expiration_date) VALUES
('Laptop', 'High-performance laptop for developers', 1299.99, '2030-12-31'),
('Smartphone', 'Latest generation smartphone', 899.50, '2031-01-15'),
('Headphones', 'Noise-cancelling over-ear headphones', 199.99, NULL),  -- No expiration
('Monitor', '27-inch 4K UHD display', 349.75, '2030-07-01'),
('Keyboard', 'Mechanical keyboard with RGB backlight', 89.90, '2032-05-20');

