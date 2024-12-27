@echo off
cd C:\Users\ayyoub\Documents\InventoryRMIProject2

java -cp ".;bin;lib/mysql-connector-java-9.1.0.jar" -Djava.security.policy=policy/server.policy server.ServerMain
pause
