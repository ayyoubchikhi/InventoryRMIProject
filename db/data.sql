-- Données initiales pour les tests
INSERT INTO products (name, category, quantity, price) VALUES
('Laptop', 'Electronics', 10, 999.99),
('Mouse', 'Electronics', 50, 19.99),
('Chair', 'Furniture', 20, 49.99);

INSERT INTO users (username, password) VALUES
('admin', 'admin123'); -- Attention : à remplacer par un hash dans une application réelle
