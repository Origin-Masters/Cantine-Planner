package de.htwsaar.cantineplanner.dbUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DataBaseUtil {

    private static final String DATABASE_NAME = "database.db";

    /**
     * Load the database from resources and copy it to the file system
     *
     * @param targetPath   Name des Pfades wohin die DB soll ("./database/database.db")
     */
    public static void loadInitialDataBase(String targetPath) {

        // InputStream zum LEsen der Datenbank aus dem mitgelierferten FAT jarr
        try (InputStream inputStream = DataBaseUtil.class.getClassLoader().getResourceAsStream(DATABASE_NAME)) {

            if (inputStream == null) {
                System.out.println("Die Datenbank wurde leider nicht gefunden: " + DATABASE_NAME);
                return;
            }

            Path target = Paths.get(targetPath);

            // Sicherstellen, dass der Ordner existiert
            // Falls nicht, wird er erstellt
            Files.createDirectories(target.getParent());


            if(target.toFile().exists()){
                return;
            }
            // Datei kopieren (binär)
            // kopiert Datei von InputStream nach Target
            Files.copy(inputStream, target);

            System.out.println("Datenbank erfolgreich geladen: " + DATABASE_NAME + " -> " + targetPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }








}
