package de.htwsaar.cantineplanner.businessLogic;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int userID;
    private String username;
    private String password;
    private String email;
    private String role;
    private List<Allergen> allergens;

    public User() {
        System.out.println("Basic User created");
        this.userID = 0;
        this.username = "Test";
        this.password = "password";
        this.email = "max.mustermann@gmail.com";
        this.role = "User";
        this.allergens = new ArrayList<>();
    }

    public User(int userID, String username, String password, String email, String role, List<Allergen> allergens) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.allergens = allergens;
    }
    public void addAllergen(Allergen allergen) {
        if(!this.allergens.contains(allergen)){
            this.allergens.add(allergen);
        }
    }
    public void removeAllergen(Allergen allergen) {
        if(!this.allergens.contains(allergen)){
            this.allergens.add(allergen);
        }
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void getAllergens() {
        for (Allergen allergen : allergens) {
            System.out.println(allergen);
        }
    }
    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", allergens=" + allergens +
                '}';
    }
}
