@echo off
cd C:\Users\ayyoub\Documents\InventoryRMIProject2

javac -cp ".;lib/mysql-connector-java-9.1.0.jar" -d bin server/InventoryInterface.java server/InventoryImplementation.java server/ServerMain.java server/DAO/ProductDAO.java server/DAO/ProductDAO.java 

pause