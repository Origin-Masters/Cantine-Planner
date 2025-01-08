package de.htwsaar.cantineplanner.businessLogic;

public enum Allergen {
    PEANUTS("Peanuts"),
    TREE_NUTS("Tree Nuts"), // e.g., almonds, walnuts, cashews
    MILK("Milk"),
    EGGS("Eggs"),
    FISH("Fish"),
    SHELLFISH("Shellfish"), // e.g., shrimp, crab, lobster
    WHEAT("Wheat"),
    SOY("Soy"),
    SESAME("Sesame"),
    MUSTARD("Mustard"),
    LUPIN("Lupin"),
    SULFITES("Sulfites"),
    CELERY("Celery");

    private final String description;

    Allergen(String description) {
        this.description = description;
    }
}
