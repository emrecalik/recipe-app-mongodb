package com.edoras.recipeproject.converters;

import com.edoras.recipeproject.commands.IngredientCommand;
import com.edoras.recipeproject.domains.Ingredient;
import com.edoras.recipeproject.domains.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IngredientCommandToIngredient implements Converter<IngredientCommand, Ingredient> {

    private final UomCommandToUom uomCommandToUom;

    @Autowired
    public IngredientCommandToIngredient(UomCommandToUom uomCommandToUom){
        this.uomCommandToUom = uomCommandToUom;
    }

    @Override
    public Ingredient convert(IngredientCommand ingredientCommand) {
        if(ingredientCommand == null) {
            return null;
        }

        Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientCommand.getId());
        if (ingredientCommand.getRecipeId() != null) {
            Recipe recipe = new Recipe();
            recipe.setId(ingredientCommand.getRecipeId());
            ingredient.setRecipe(recipe);
            recipe.addIngredients(ingredient);
        }
        ingredient.setDescription(ingredientCommand.getDescription());
        ingredient.setAmount(ingredientCommand.getAmount());
        ingredient.setUnitOfMeasure(uomCommandToUom.convert(ingredientCommand.getUnitOfMeasureCommand()));

        return ingredient;
    }
}
