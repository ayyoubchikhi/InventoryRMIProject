package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {
    public static void main(String[] args) {
        try {
            // Suppression de la partie SecurityManager car dépréciée
            Registry registry = LocateRegistry.createRegistry(1099);
            InventoryInterface inventoryService = new InventoryImplementation();
            registry.rebind("InventoryService", inventoryService);
            
            System.out.println("Inventory server started...");
        } catch (Exception e) {
            System.err.println("Server startup error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}