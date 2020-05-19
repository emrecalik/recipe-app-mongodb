package com.edoras.recipeproject.converters;

import com.edoras.recipeproject.commands.RecipeCommand;
import com.edoras.recipeproject.domains.Recipe;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RecipeCommandToRecipe implements Converter<RecipeCommand, Recipe> {

    private final NotesCommandToNotes notesCommandConverter;
    private final IngredientCommandToIngredient ingredientCommandConverter;
    private final CategoryCommandToCategory categoryCommandConverter;

    public RecipeCommandToRecipe(NotesCommandToNotes notesCommandConverter,
                                 IngredientCommandToIngredient ingredientCommandConverter,
                                 CategoryCommandToCategory categoryCommandConverter) {
        this.notesCommandConverter = notesCommandConverter;
        this.ingredientCommandConverter = ingredientCommandConverter;
        this.categoryCommandConverter = categoryCommandConverter;
    }

    @Override
    public Recipe convert(RecipeCommand recipeCommand) {
        if (recipeCommand == null) {
            return null;
        }

        Recipe recipe = new Recipe();
        recipe.setId(recipeCommand.getId());
        recipe.setDescription(recipeCommand.getDescription());
        recipe.setPrepTime(recipeCommand.getPrepTime());
        recipe.setCookTime(recipeCommand.getCookTime());
        recipe.setServings(recipeCommand.getServings());
        recipe.setSource(recipeCommand.getSource());
        recipe.setUrl(recipeCommand.getUrl());
        recipe.setDirections(recipeCommand.getDirections());
        recipe.setNotes(notesCommandConverter.convert(recipeCommand.getNotesCommand()));

        if (recipeCommand.getIngredientCommands() != null && recipeCommand.getIngredientCommands().size() > 0) {
            recipeCommand.getIngredientCommands().forEach(ingredientCommand -> {
                recipe.getIngredients().add(ingredientCommandConverter.convert(ingredientCommand));
            });
        }

        if (recipeCommand.getCategoryCommands() != null && recipeCommand.getCategoryCommands().size() > 0) {
            recipeCommand.getCategoryCommands().forEach(categoryCommand -> {
                recipe.getCategories().add(categoryCommandConverter.convert(categoryCommand));
            });
        }

        recipe.setDifficulty(recipeCommand.getDifficulty());

        return recipe;
    }
}
