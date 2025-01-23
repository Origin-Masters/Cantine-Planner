package de.htwsaar.cantineplanner.businessLogic;

import java.util.HashMap;
import java.util.Map;

public class AllergenMapper {
    private static final Map<String, String> allergenMap = new HashMap<>();

    static {
        allergenMap.put("F", "Fisch");
        allergenMap.put("N", "Nüsse");
        allergenMap.put("G", "Gluten");
        allergenMap.put("M", "Milch");
        allergenMap.put("E", "Eier");
        allergenMap.put("S", "Soja");
        allergenMap.put("C", "Sellerie");
        allergenMap.put("U", "Senf");
        allergenMap.put("T", "Sesam");
        allergenMap.put("L", "Lupinen");
        allergenMap.put("P", "Erdnüsse");
        allergenMap.put("I", "Sulfite");
        allergenMap.put("K", "Krebstiere");
        allergenMap.put("W", "Weichtiere");
        allergenMap.put("H", "Schalenfrüchte");
    }

    public static String getAllergenFullName(String code) {
        return allergenMap.getOrDefault(code, code);
    }
}