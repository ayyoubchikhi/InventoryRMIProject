package server;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String LOG_FILE = "logs.txt";

    public static void log(String message) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write("[" + timestamp + "] " + message + "\n");
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du log : " + e.getMessage());
        }
    }
}
