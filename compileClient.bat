@echo off
set JAVAFX_PATH=javafx-sdk\lib
javac --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml -cp ".;bin;lib/mysql-connector-java-9.1.0.jar" -d bin client/ClientGUI.java
pause