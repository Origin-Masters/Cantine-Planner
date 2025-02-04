package de.htwsaar.cantineplanner.dbUtils;

import java.io.*;
import java.nio.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DataBaseUtil {
    /**
     * Load the database from resources and copy it to the file system
     *
     * @param databaseName Name der Datenbank im FAT JAR ("database.db")
     * @param targetPath   Name des Pfades wohin die DB soll ("./database/database.db")
     */
    public static void loadDataBase(String databaseName, String targetPath) {

        // InputStream zum LEsen der Datenbank aus dem mitgelierferten FAT jarr
        try (InputStream inputStream = DataBaseUtil.class.getClassLoader().getResourceAsStream(databaseName)) {

            if (inputStream == null) {
                System.out.println("Die Datenbank wurde leider nicht gefunden: " + databaseName);
                return;
            }

            Path target = Paths.get(targetPath);

            // Sicherstellen, dass der Ordner existiert
            // Falls nicht, wird er erstellt
            Files.createDirectories(target.getParent());

            // Datei kopieren (binär)
            // kopiert Datei von InputStream nach Target
            Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Datenbank erfolgreich geladen: " + databaseName + " -> " + targetPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }








}
