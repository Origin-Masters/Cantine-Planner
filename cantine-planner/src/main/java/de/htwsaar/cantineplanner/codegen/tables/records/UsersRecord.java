/*
 * This file is generated by jOOQ.
 */
package de.htwsaar.cantineplanner.codegen.tables.records;


import de.htwsaar.cantineplanner.codegen.tables.Users;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class UsersRecord extends UpdatableRecordImpl<UsersRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>users.userid</code>.
     */
    public void setUserid(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>users.userid</code>.
     */
    public Integer getUserid() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>users.username</code>.
     */
    public void setUsername(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>users.username</code>.
     */
    public String getUsername() {
        return (String) get(1);
    }

    /**
     * Setter for <code>users.password</code>.
     */
    public void setPassword(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>users.password</code>.
     */
    public String getPassword() {
        return (String) get(2);
    }

    /**
     * Setter for <code>users.email</code>.
     */
    public void setEmail(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>users.email</code>.
     */
    public String getEmail() {
        return (String) get(3);
    }

    /**
     * Setter for <code>users.role</code>.
     */
    public void setRole(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>users.role</code>.
     */
    public Integer getRole() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>users.fav_meal</code>.
     */
    public void setFavMeal(Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>users.fav_meal</code>.
     */
    public Integer getFavMeal() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>users.dont_show_meal</code>.
     */
    public void setDontShowMeal(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>users.dont_show_meal</code>.
     */
    public String getDontShowMeal() {
        return (String) get(6);
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
     * Create a detached UsersRecord
     */
    public UsersRecord() {
        super(Users.USERS);
    }

    /**
     * Create a detached, initialised UsersRecord
     */
    public UsersRecord(Integer userid, String username, String password, String email, Integer role, Integer favMeal, String dontShowMeal) {
        super(Users.USERS);

        setUserid(userid);
        setUsername(username);
        setPassword(password);
        setEmail(email);
        setRole(role);
        setFavMeal(favMeal);
        setDontShowMeal(dontShowMeal);
        resetChangedOnNotNull();
    }
}
