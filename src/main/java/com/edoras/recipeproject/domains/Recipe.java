package com.edoras.recipeproject.domains;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Recipe {

    private String id;

    private String description;
    private int prepTime;
    private int cookTime;
    private int servings;
    private String source;
    private String url;

    private String directions;

    private Notes notes;

    private Set<Ingredient> ingredients = new HashSet<>();

    private Set<Category> categories = new HashSet<>();

    private Difficulty difficulty;

    private Byte[] image;

    public void setNotes(Notes notes) {
        if (notes != null) {
            this.notes = notes;
            notes.setRecipe(this);
        }
    }

    public Recipe addIngredients(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        ingredient.setRecipe(this);
        return this;
    }
}
