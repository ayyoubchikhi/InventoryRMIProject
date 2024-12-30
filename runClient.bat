@echo off
set JAVAFX_PATH=javafx-sdk\lib
java --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml -cp ".;bin;lib/mysql-connector-java-9.1.0.jar" client.ClientGUI
pause