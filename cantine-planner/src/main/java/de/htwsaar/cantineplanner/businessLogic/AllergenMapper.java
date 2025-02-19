package de.htwsaar.cantineplanner.businessLogic;

import java.util.HashMap;
import java.util.Map;

public class AllergenMapper {
    private static final Map<String, String> allergenMap = new HashMap<>();

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

    public static String getAllergenFullName(String code) {
        return allergenMap.getOrDefault(code, code);
    }

    public static String getAllergenCode(String fullName) {
        for (Map.Entry<String, String> entry : allergenMap.entrySet()) {
            if (entry.getValue().equals(fullName)) {
                return entry.getKey();
            }
        }
        return null; // Return the full name if no code is found
    }
}