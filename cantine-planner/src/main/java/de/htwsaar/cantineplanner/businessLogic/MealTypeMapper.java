package de.htwsaar.cantineplanner.businessLogic;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The {@code MealTypeMapper} class provides a mapping between meal type identifiers and their corresponding names.
 * It allows retrieval of a meal type name based on an integer ID.
 */
public class MealTypeMapper {

    /**
     * A static map that holds the association between meal type IDs and their names.
     */
    private static final Map<Integer, String> mealTypeMap = new HashMap<>();

    // Static initialization block that populates the mealTypeMap with predefined meal type entries.
    static {
        mealTypeMap.put(0, "Meat");
        mealTypeMap.put(1, "Vegetarian");
        mealTypeMap.put(2, "Vegan");
    }

    /**
     * Retrieves the meal type name corresponding to the given meal type ID.
     * If the provided ID is {@code null} or no mapping exists for the ID,
     * it returns "Unknown Meal Type".
     *
     * @param id the meal type identifier
     * @return the corresponding meal type name, or "Unknown Meal Type" if the ID is not found
     */
    public static String getMealTypeName(Integer id) {
        return Optional.ofNullable(id)
                .map(mealTypeMap::get)
                .orElse("Unknown Meal Type");
    }
}
