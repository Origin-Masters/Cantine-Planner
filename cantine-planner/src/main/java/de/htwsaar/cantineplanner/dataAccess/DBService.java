package de.htwsaar.cantineplanner.dataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBService {

    public void fetchAllMeals() {
        String query = "SELECT * FROM meals";

        try (Connection connection = HikariCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("Ergebnisse der Abfrage:");
            while (resultSet.next()) {
                System.out.println("Gericht: " + resultSet.getString("Name"));
                System.out.println("Preis: " + resultSet.getDouble("Price"));
                System.out.println("Kalorien: " + resultSet.getInt("calories"));
                System.out.println("---------");
            }
        } catch (Exception e) {
            System.err.println("Fehler bei der Abfrage:");
            e.printStackTrace();
        }
    }
}
