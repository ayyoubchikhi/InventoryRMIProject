package server;

import server.DAO.ProductDAO;
import server.DAO.UserDAO;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class InventoryImplementation extends UnicastRemoteObject implements InventoryInterface {
    private final Connection connection;
    private final ProductDAO productDAO;
    private final UserDAO userDAO;

    public InventoryImplementation() throws RemoteException {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/inventory_db?useSSL=false&allowPublicKeyRetrieval=true",
            "root",
            ""
        );
        
        productDAO = new ProductDAO(connection);
        userDAO = new UserDAO(connection);
        
        // Correction du shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                closeConnection();
            } catch (RemoteException e) {
                System.err.println("Error closing connection during shutdown: " + e.getMessage());
            }
        }));
        
    } catch (ClassNotFoundException | SQLException e) {
        System.err.println("Error connecting to database: " + e.getMessage());
        throw new RemoteException("Database connection error", e);
    }
}

    @Override
    public List<String> getAllProducts() throws RemoteException {
        try {
            return productDAO.getAllProductNames();
        } catch (SQLException e) {
            throw new RemoteException("Error retrieving products", e);
        }
    }

    @Override
    public void addProduct(String name, String category, int quantity, double price) throws RemoteException {
        try {
            if (name == null || name.trim().isEmpty()) {
                throw new RemoteException("Product name cannot be empty");
            }
            if (quantity < 0) {
                throw new RemoteException("Quantity cannot be negative");
            }
            if (price < 0) {
                throw new RemoteException("Price cannot be negative");
            }
            productDAO.addProduct(name, category, quantity, price);
        } catch (SQLException e) {
            throw new RemoteException("Error adding product", e);
        }
    }

    @Override
    public void updateProductQuantity(String name, int quantity) throws RemoteException {
        try {
            if (quantity < 0) {
                throw new RemoteException("Quantity cannot be negative");
            }
            productDAO.updateProductQuantity(name, quantity);
        } catch (SQLException e) {
            throw new RemoteException("Error updating quantity", e);
        }
    }

    @Override
    public void removeProduct(String name) throws RemoteException {
        try {
            productDAO.removeProduct(name);
        } catch (SQLException e) {
            throw new RemoteException("Error removing product", e);
        }
    }

    @Override
    public List<String> searchProductsByName(String name) throws RemoteException {
        try {
            return productDAO.searchProductsByName(name);
        } catch (SQLException e) {
            throw new RemoteException("Error searching by name", e);
        }
    }

    @Override
    public List<String> searchProductsByCategory(String category) throws RemoteException {
        try {
            return productDAO.searchProductsByCategory(category);
        } catch (SQLException e) {
            throw new RemoteException("Error searching by category", e);
        }
    }

    @Override
    public List<String> searchProductsByStock(int minStock) throws RemoteException {
        try {
            return productDAO.searchProductsByStock(minStock);
        } catch (SQLException e) {
            throw new RemoteException("Error searching by stock", e);
        }
    }

    @Override
    public boolean authenticate(String username, String password) throws RemoteException {
        try {
            return userDAO.authenticate(username, password);
        } catch (SQLException e) {
            throw new RemoteException("Authentication error", e);
        }
    }

    @Override
    public void closeConnection() throws RemoteException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            throw new RemoteException("Error closing database connection", e);
        }
    }
}

