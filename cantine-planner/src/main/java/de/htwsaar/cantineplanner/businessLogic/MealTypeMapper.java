package de.htwsaar.cantineplanner.businessLogic;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MealTypeMapper {

    private static final Map<Integer, String> mealTypeMap = new HashMap<>();

    static {
        mealTypeMap.put(0, "Meat");
        mealTypeMap.put(1, "Vegetarian");
        mealTypeMap.put(2, "Vegan");
        /*
        mealTypeMap.put(4, "Fish");
        mealTypeMap.put(5, "Dessert");
        */
    }


public static String getMealTypeName(Integer id) {
    return Optional.ofNullable(id)
            .map(mealTypeMap::get)
            .orElse("Unknown Meal Type");
}
}
