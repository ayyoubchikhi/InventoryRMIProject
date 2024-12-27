-- Créer la base de données
CREATE DATABASE IF NOT EXISTS inventory_db;
USE inventory_db;

-- Créer la table des produits
CREATE TABLE IF NOT EXISTS products (
    name VARCHAR(100) PRIMARY KEY,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);

-- Exemple d'insertion de données
INSERT INTO products (name, quantity, price) VALUES 
('Ordinateur Portable', 10, 999.99),
('Smartphone', 25, 499.50),
('Tablette', 15, 299.99);