@echo off
cd C:\Users\ayyoub\Documents\InventoryRMIProject2

javac -cp ".;lib/mysql-connector-java-9.1.0.jar" -d bin server/*.java server/DAO/*.java 
pause