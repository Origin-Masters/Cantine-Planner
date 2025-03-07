package de.htwsaar.cantineplanner.businessLogic;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code AllergenMapper} class provides methods for mapping allergen codes to their full names and vice versa.
 */
public class AllergenMapper {
    private static final Map<String, String> allergenMap = new HashMap<>();

    // Static initialization block that populates the allergenMap with predefined allergen codes and names.
    static {
        allergenMap.put("F", "Fish");
        allergenMap.put("N", "Nuts");
        allergenMap.put("G", "Gluten");
        allergenMap.put("M", "Milk");
        allergenMap.put("E", "Eggs");
        allergenMap.put("S", "Soy");
        allergenMap.put("C", "Celery");
        allergenMap.put("U", "Mustard");
        allergenMap.put("T", "Sesame");
        allergenMap.put("L", "Lupin");
        allergenMap.put("P", "Peanuts");
        allergenMap.put("I", "Sulfites");
        allergenMap.put("K", "Crustaceans");
        allergenMap.put("W", "Molluscs");
        allergenMap.put("H", "Tree Nuts");
    }

    /**
     * Retrieves the full name of the allergen corresponding to the given code.
     * If no mapping is found, the provided code is returned.
     *
     * @param code the allergen code
     * @return the full name of the allergen, or the code if no mapping exists
     */
    public static String getAllergenFullName(String code) {
        return allergenMap.getOrDefault(code, code);
    }

    /**
     * Retrieves the allergen code corresponding to the given full name.
     * Iterates over the entries of the allergenMap to find the matching code.
     *
     * @param fullName the full name of the allergen
     * @return the allergen code, or {@code null} if no matching code is found
     */
    public static String getAllergenCode(String fullName) {
        for (Map.Entry<String, String> entry : allergenMap.entrySet()) {
            if (entry.getValue().equals(fullName)) {
                return entry.getKey();
            }
        }
        return null; // Return null if no matching code is found
    }
}
