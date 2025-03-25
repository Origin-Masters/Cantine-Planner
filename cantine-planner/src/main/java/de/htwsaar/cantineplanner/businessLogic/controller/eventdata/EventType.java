package de.htwsaar.cantineplanner.businessLogic.controller.eventdata;

public enum EventType {
    LOGIN(StringArrayData.class),
    REGISTER(ArrayListData.class),
    SHOW_REGISTER_SCREEN(),
    SWITCH_MENU(IntData.class),
    EXIT(),
    LOGOUT(),

    SHOW_ALL_MEALS(),
    SHOW_ALL_ALLERGIES(),
    SHOW_ADD_MEAL(),
    SHOW_DELETE_MEAL(),
    ADD_MEAL(ArrayListData.class),
    DELETE_MEAL(ArrayListData.class),
    SHOW_MEAL_DETAILS_BY_ID(ArrayListData.class),
    MEAL_DETAILS_BY_ID(),
    SHOW_SEARCH_MEAL_BY_NAME(),
    SEARCH_MEAL_BY_NAME(ArrayListData.class),
    SHOW_EDIT_MEAL(),
    EDIT_MEAL(ArrayListData.class),
    SHOW_SORT_MEALS(),
    SORT_MEALS_BY_PRICE(),
    SORT_MEALS_BY_NAME(),
    SORT_MEALS_BY_RATING(),
    SORT_MEALS_BY_ALLERGENS(),
    SORT_MEALS_BY_CALORIES(),

    SHOW_ADD_REVIEW(),
    ADD_REVIEW(ArrayListData.class),
    SHOW_DELETE_REVIEW(),
    DELETE_REVIEW(ArrayListData.class),
    SHOW_ALL_REVIEWS(),
    SHOW_SEARCH_REVIEWS_BY_MEAL_NAME(),
    SEARCH_REVIEWS_BY_MEAL_NAME(ArrayListData.class),
    SHOW_EDIT_USER_DATA(),
    EDIT_USER_DATA(ArrayListData.class),
    SHOW_EDIT_NEW_USER_DATA(),
    EDIT_NEW_USER_DATA(StringArrayData.class),
    SHOW_REVIEWS_BY_USER(),
    SHOW_ADMIN_MENU(),
    SHOW_ALLERGEN_SETTINGS(),
    ALLERGEN_SETTINGS(),
    SHOW_ALL_USERS(),
    SHOW_DELETE_USER(),
    DELETE_USER(ArrayListData.class),
    SHOW_UPDATE_USER_ROLE(),
    UPDATE_USER_ROLE(ArrayListData.class),

    SHOW_WEEKLY_PLAN(),
    EDIT_WEEKLY_PLAN(),
    RESET_WEEKLY_PLAN(),
    EDIT_WEEKLY_PLAN_MONDAY(),
    EDIT_WEEKLY_PLAN_MONDAY_SUBMIT(ArrayListData.class),
    EDIT_WEEKLY_PLAN_TUESDAY(),
    EDIT_WEEKLY_PLAN_TUESDAY_SUBMIT(ArrayListData.class),
    EDIT_WEEKLY_PLAN_WEDNESDAY(),
    EDIT_WEEKLY_PLAN_WEDNESDAY_SUBMIT(ArrayListData.class),
    EDIT_WEEKLY_PLAN_THURSDAY(),
    EDIT_WEEKLY_PLAN_THURSDAY_SUBMIT(ArrayListData.class),
    EDIT_WEEKLY_PLAN_FRIDAY(),
    EDIT_WEEKLY_PLAN_FRIDAY_SUBMIT(ArrayListData.class),



    SHOW_ERROR_SCREEN(ShowErrorScreenData.class),
    SHOW_SUCCESS_SCREEN(ShowSuccessScreenData.class),
    ALLERGENE_SETTINGS(AllergeneSettingsData.class),
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
        if(eventData == null) {
            System.out.println(eventDataClass.getSimpleName() + " with null data");
        } else {
            System.out.println(eventData.getClass().getSimpleName() + " " + eventDataClass.getSimpleName());
        }
        return eventDataClass.isInstance(eventData);
    }
}
