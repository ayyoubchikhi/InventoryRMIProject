package server.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private final Connection connection;

    public ProductDAO(Connection connection) {
        this.connection = connection;
    }

    public List<String> getAllProductNames() throws SQLException {
        List<String> productNames = new ArrayList<>();
        String query = "SELECT name, category, quantity, price FROM products";
        
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                productNames.add(String.format("%s (Cat: %s, Qty: %d, Price: %.2f)",
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getInt("quantity"),
                    rs.getDouble("price")));
            }
            return productNames;
        }
    }

    public void addProduct(String name, String category, int quantity, double price) throws SQLException {
        String query = "INSERT INTO products (name, category, quantity, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            try {
                stmt.setString(1, name);
                stmt.setString(2, category);
                stmt.setInt(3, quantity);
                stmt.setDouble(4, price);
                stmt.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public void updateProductQuantity(String name, int quantity) throws SQLException {
        String query = "UPDATE products SET quantity = ? WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            try {
                stmt.setInt(1, quantity);
                stmt.setString(2, name);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Product not found: " + name);
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public void removeProduct(String name) throws SQLException {
        String query = "DELETE FROM products WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            try {
                stmt.setString(1, name);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Product not found: " + name);
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public List<String> searchProductsByName(String name) throws SQLException {
        List<String> products = new ArrayList<>();
        String query = "SELECT name, category, quantity, price FROM products WHERE name LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + name + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(String.format("%s (Cat: %s, Qty: %d, Price: %.2f)",
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")));
                }
            }
            return products;
        }
    }

    public List<String> searchProductsByCategory(String category) throws SQLException {
        List<String> products = new ArrayList<>();
        String query = "SELECT name, category, quantity, price FROM products WHERE category LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + category + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(String.format("%s (Cat: %s, Qty: %d, Price: %.2f)",
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")));
                }
            }
            return products;
        }
    }

    public List<String> searchProductsByStock(int minStock) throws SQLException {
        List<String> products = new ArrayList<>();
        String query = "SELECT name, category, quantity, price FROM products WHERE quantity >= ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, minStock);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(String.format("%s (Cat: %s, Qty: %d, Price: %.2f)",
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")));
                }
            }
            return products;
        }
    }
}