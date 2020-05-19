package com.edoras.recipeproject.services;

import com.edoras.recipeproject.commands.IngredientCommand;

public interface IngredientService {
    IngredientCommand findCommandByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);
    IngredientCommand save(IngredientCommand ingredientCommand);
    void deleteById(Long recipeId, Long ingredientId);
}
