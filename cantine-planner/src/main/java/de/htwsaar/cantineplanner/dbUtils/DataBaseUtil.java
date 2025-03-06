package de.htwsaar.cantineplanner.dbUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for database operations.
 */
public class DataBaseUtil {

    private static final String DATABASE_NAME = "database.db";

    /**
     * Loads the database from the application resources and copies it to the file system.
     *
     * @param targetPath the target path where the database should be placed (e.g., "./database/database.db")
     */
    public static void loadInitialDataBase(String targetPath) {

        // Open an InputStream to read the database from the bundled FAT jar.
        try (InputStream inputStream = DataBaseUtil.class.getClassLoader().getResourceAsStream(DATABASE_NAME)) {

            if (inputStream == null) {
                System.out.println("The database was not found: " + DATABASE_NAME);
                return;
            }

            Path target = Paths.get(targetPath);

            // Ensure that the target directory exists.
            // If it does not exist, it will be created.
            Files.createDirectories(target.getParent());

            if (target.toFile().exists()) {
                return;
            }
            // Copy the file in binary mode.
            // Copies the file from the InputStream to the target path.
            Files.copy(inputStream, target);

            System.out.println("Database loaded successfully: " + DATABASE_NAME + " -> " + targetPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
