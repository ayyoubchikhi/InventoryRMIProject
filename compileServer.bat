@echo off

javac -cp ".;lib/mysql-connector-java-9.1.0.jar" -d bin server/*.java server/DAO/*.java 
pause