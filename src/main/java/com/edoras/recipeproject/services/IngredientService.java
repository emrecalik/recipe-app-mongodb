package com.edoras.recipeproject.services;

import com.edoras.recipeproject.commands.IngredientCommand;
import reactor.core.publisher.Mono;

public interface IngredientService {
    Mono<IngredientCommand> findCommandByRecipeIdAndIngredientId(String recipeId, String ingredientId);
    Mono<IngredientCommand> save(IngredientCommand ingredientCommand);
    Mono<Void> deleteById(String recipeId, String ingredientId);
}
