/*
 * This file is generated by jOOQ.
 */
package de.htwsaar.cantineplanner.codegen.tables.records;


import de.htwsaar.cantineplanner.codegen.tables.Meals;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class MealsRecord extends UpdatableRecordImpl<MealsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>meals.meal_id</code>.
     */
    public void setMealId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>meals.meal_id</code>.
     */
    public Integer getMealId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>meals.Name</code>.
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>meals.Name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>meals.Price</code>.
     */
    public void setPrice(Float value) {
        set(2, value);
    }

    /**
     * Getter for <code>meals.Price</code>.
     */
    public Float getPrice() {
        return (Float) get(2);
    }

    /**
     * Setter for <code>meals.calories</code>.
     */
    public void setCalories(Integer value) {
        set(3, value);
    }

    /**
     * Getter for <code>meals.calories</code>.
     */
    public Integer getCalories() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>meals.allergy</code>.
     */
    public void setAllergy(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>meals.allergy</code>.
     */
    public String getAllergy() {
        return (String) get(4);
    }

    /**
     * Setter for <code>meals.meat</code>.
     */
    public void setMeat(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>meals.meat</code>.
     */
    public Integer getMeat() {
        return (Integer) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached MealsRecord
     */
    public MealsRecord() {
        super(Meals.MEALS);
    }

    /**
     * Create a detached, initialised MealsRecord
     */
    public MealsRecord(Integer mealId, String name, Float price, Integer calories, String allergy, Integer meat) {
        super(Meals.MEALS);

        setMealId(mealId);
        setName(name);
        setPrice(price);
        setCalories(calories);
        setAllergy(allergy);
        setMeat(meat);
        resetChangedOnNotNull();
    }
}
