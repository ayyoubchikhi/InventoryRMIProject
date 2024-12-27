package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface InventoryInterface extends Remote {
    void addProduct(String name, String category, int quantity, double price) throws RemoteException;
    void updateProductQuantity(String name, int quantity) throws RemoteException;
    void removeProduct(String name) throws RemoteException;
    List<String> searchProductsByName(String name) throws RemoteException;
    List<String> searchProductsByCategory(String category) throws RemoteException;
    List<String> searchProductsByStock(int minStock) throws RemoteException;
    boolean authenticate(String username, String password) throws RemoteException;
    List<String> getAllProducts() throws RemoteException;
    void closeConnection() throws RemoteException;
}