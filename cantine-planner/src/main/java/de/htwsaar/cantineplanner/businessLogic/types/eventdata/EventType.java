package de.htwsaar.cantineplanner.businessLogic.types.eventdata;

public enum EventType {
    LOGIN(StringArrayData.class),
    REGISTER(StringArrayData.class),
    SHOW_REGISTER_SCREEN(),
    SWITCH_MENU(IntData.class),
    EXIT(),
    LOGOUT(),

    SHOW_ALL_MEALS(),
    SHOW_ALL_ALLERGIES(),
    SHOW_ADD_MEAL(),
    SHOW_DELETE_MEAL(),
    ADD_MEAL(StringArrayData.class),
    DELETE_MEAL(StringArrayData.class),
    SHOW_SEARCH_MEAL_BY_ID(),
    SHOW_MEAL_BY_ID(StringArrayData.class),
    SHOW_SEARCH_MEAL_BY_NAME(),
    SHOW_MEAL_BY_NAME(StringArrayData.class),
    SHOW_EDIT_MEAL(),
    EDIT_MEAL(StringArrayData.class),
    SHOW_SORT_MEALS(),
    SORT_MEALS_BY_PRICE(),
    SORT_MEALS_BY_NAME(),
    SORT_MEALS_BY_RATING(),
    SORT_MEALS_BY_ALLERGENS(),
    SORT_MEALS_BY_CALORIES(),

    SHOW_ADD_REVIEW(),
    ADD_REVIEW(StringArrayData.class),
    SHOW_DELETE_REVIEW(),
    DELETE_REVIEW(StringArrayData.class),
    SHOW_ALL_REVIEWS(),
    SHOW_SEARCH_REVIEWS_BY_MEAL_NAME(),
    SEARCH_REVIEWS_BY_MEAL_NAME(StringArrayData.class),
    SHOW_EDIT_USER_DATA(),
    EDIT_USER_DATA(StringArrayData.class),
    SHOW_EDIT_NEW_USER_DATA(),
    EDIT_NEW_USER_DATA(StringArrayData.class),
    SHOW_REVIEWS_BY_USER(),
    SHOW_ADMIN_MENU(),
    SHOW_ALLERGEN_SETTINGS(),
    ALLERGEN_SETTINGS(),
    SHOW_ALL_USERS(),
    SHOW_DELETE_USER(),
    DELETE_USER(StringArrayData.class),
    SHOW_UPDATE_USER_ROLE(),
    UPDATE_USER_ROLE(StringArrayData.class),

    SHOW_WEEKLY_PLAN(),
    EDIT_WEEKLY_PLAN(),
    RESET_WEEKLY_PLAN(),
    EDIT_WEEKLY_PLAN_MONDAY(),
    EDIT_WEEKLY_PLAN_WEEKDAY(IntData.class),
    EDIT_WEEKLY_PLAN_WEEKDAY_SUBMIT(StringArrayData.class),

    EDIT_WEEKLY_PLAN_MONDAY_SUBMIT(StringArrayData.class),
    EDIT_WEEKLY_PLAN_TUESDAY(),
    EDIT_WEEKLY_PLAN_TUESDAY_SUBMIT(StringArrayData.class),
    EDIT_WEEKLY_PLAN_WEDNESDAY(),
    EDIT_WEEKLY_PLAN_WEDNESDAY_SUBMIT(StringArrayData.class),
    EDIT_WEEKLY_PLAN_THURSDAY(),
    EDIT_WEEKLY_PLAN_THURSDAY_SUBMIT(StringArrayData.class),
    EDIT_WEEKLY_PLAN_FRIDAY(),
    EDIT_WEEKLY_PLAN_FRIDAY_SUBMIT(StringArrayData.class),



    SHOW_ERROR_SCREEN(StringData.class),
    SHOW_SUCCESS_SCREEN(StringData.class),
    ALLERGENE_SETTINGS(StringArrayData.class),
    ;


    private final Class<? extends EventData> eventDataClass;
    EventType(Class<? extends EventData> eventDataClass) {
        this.eventDataClass = eventDataClass;
    }

    EventType() {
        this(null);
    }

    public Class<? extends EventData> getEventDataClass() {
        return eventDataClass;
    }

    public boolean verifyEventData(EventData eventData) {
        if(eventDataClass == null) {
            return true;
        }
        return eventDataClass.isInstance(eventData);
    }
}
