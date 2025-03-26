package de.htwsaar.cantineplanner.businessLogic.controller;

public enum Weekday {
    MONDAY("Mon", "Monday", 1),
    TUESDAY("Tue", "Tuesday", 2),
    WEDNESDAY("Wed", "Wednesday", 3),
    THURSDAY("Thu", "Thursday", 4),
    FRIDAY("Fri", "Friday", 5);

    private final String shortName;
    private final String displayName;
    private final int sortOrder;

    Weekday(String shortName, String displayName, int sortOrder) {
        this.shortName = shortName;
        this.displayName = displayName;
        this.sortOrder = sortOrder;
    }

    public String getShortName() {
        return shortName;
    }

    public String getDisplayName() {
        return displayName;
    }
    /**
     * Finds a weekday by its sort order and returns its display name.
     *
     * @param sortNumber the sort order number to find (1-5)
     * @return the display name of the weekday with the matching sort order, or null if not found
     */
    public static String getDisplayNameBySortOrder(int sortNumber) {
        for (Weekday day : Weekday.values()) {
            if (day.sortOrder == sortNumber) {
                return day.getDisplayName();
            }
        }
        return null; // Or could throw an exception if preferred
    }
    
}