package com.edoras.recipeproject.services;

import com.edoras.recipeproject.commands.RecipeCommand;
import com.edoras.recipeproject.domains.Recipe;

import java.util.List;

public interface RecipeService {
    List<Recipe> getRecipes();
    Recipe findById(Long id);
    RecipeCommand save(RecipeCommand recipeCommand);
    RecipeCommand findCommandById(Long id);
    void deleteById(Long id);
}
