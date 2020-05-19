package com.edoras.recipeproject.services;

import com.edoras.recipeproject.commands.IngredientCommand;

public interface IngredientService {
    IngredientCommand findCommandByRecipeIdAndIngredientId(String recipeId, String ingredientId);
    IngredientCommand save(IngredientCommand ingredientCommand);
    void deleteById(String recipeId, String ingredientId);
}
