package de.htwsaar.cantineplanner.businessLogic;

public enum Diet {

    VEGAN("No animal-based produce"),
    VEGETARIAN("No meat, may include dairy/eggs"),
    PESCATARIAN("Vegetarian + fish/seafood"),
    CARNIVORE("Meat-focused diet"),
    KETO("Low-carb, high-fat diet"),
    PALEO("No processed foods, dairy, grains, legumes");




    private final String description;

    Diet(String description){

       this.description = description;}

}
