package com.edoras.recipeproject.converters;

import com.edoras.recipeproject.commands.CategoryCommand;
import com.edoras.recipeproject.commands.IngredientCommand;
import com.edoras.recipeproject.commands.NotesCommand;
import com.edoras.recipeproject.commands.RecipeCommand;
import com.edoras.recipeproject.domains.Difficulty;
import com.edoras.recipeproject.domains.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecipeCommandToRecipeTest {

    RecipeCommandToRecipe recipeCommandConverter;

    private final String RECIPE_ID = "1";
    private final String RECIPE_DESCRIPTION = "my recipe";
    private final int RECIPE_PREPTIME = 7;
    private final int RECIPE_COOKTIME = 5;
    private final int RECIPE_SERVING = 3;
    private final String RECIPE_SOURCE = "source";
    private final String RECIPE_URL = "some url";
    private final String RECIPE_DIRECTIONS = "directions";
    private final Difficulty RECIPE_DIFFICULTY = Difficulty.MODERATE;

    private final String NOTES_ID = "2";
    private final String INGREDIENT_ID_1 = "3";
    private final String INGREDIENT_ID_2 = "4";
    private final String CATEGORY_ID_1 = "2";
    private final String CATEGORY_ID_2 = "3";

    @BeforeEach
    void setUp() {
        recipeCommandConverter = new RecipeCommandToRecipe(new NotesCommandToNotes(),
                new IngredientCommandToIngredient(new UomCommandToUom()),
                new CategoryCommandToCategory());
    }

    @Test
    void testNullSource() {
        assertNull(recipeCommandConverter.convert(null));
    }

    @Test
    void testEmptySource() {
        assertNotNull(recipeCommandConverter.convert(new RecipeCommand()));
    }

    @Test
    void convert() {
        // given
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(RECIPE_ID);
        recipeCommand.setDescription(RECIPE_DESCRIPTION);
        recipeCommand.setPrepTime(RECIPE_PREPTIME);
        recipeCommand.setCookTime(RECIPE_COOKTIME);
        recipeCommand.setServings(RECIPE_SERVING);
        recipeCommand.setSource(RECIPE_SOURCE);
        recipeCommand.setUrl(RECIPE_URL);
        recipeCommand.setDirections(RECIPE_DIRECTIONS);

        NotesCommand notesCommand = new NotesCommand();
        notesCommand.setId(NOTES_ID);

        recipeCommand.setNotesCommand(notesCommand);

        List<IngredientCommand> ingredientCommands = new ArrayList<>();
        IngredientCommand ingredientCommand1 = new IngredientCommand();
        ingredientCommand1.setId(INGREDIENT_ID_1);
        IngredientCommand ingredientCommand2 = new IngredientCommand();
        ingredientCommand2.setId(INGREDIENT_ID_2);
        ingredientCommands.add(ingredientCommand1);
        ingredientCommands.add(ingredientCommand2);

        recipeCommand.setIngredientCommands(ingredientCommands);

        List<CategoryCommand> categoryCommands = new ArrayList<>();
        CategoryCommand categoryCommand1 = new CategoryCommand();
        categoryCommand1.setId(CATEGORY_ID_1);
        CategoryCommand categoryCommand2 = new CategoryCommand();
        categoryCommand2.setId(CATEGORY_ID_2);
        categoryCommands.add(categoryCommand1);
        categoryCommands.add(categoryCommand2);

        recipeCommand.setCategoryCommands(categoryCommands);
        recipeCommand.setDifficulty(RECIPE_DIFFICULTY);

        // when
        Recipe recipe = recipeCommandConverter.convert(recipeCommand);

        // then
        assertEquals(RECIPE_ID, recipe.getId());
        assertEquals(RECIPE_DESCRIPTION, recipe.getDescription());
        assertEquals(RECIPE_PREPTIME, recipe.getPrepTime());
        assertEquals(RECIPE_COOKTIME, recipe.getCookTime());
        assertEquals(RECIPE_SERVING, recipe.getServings());
        assertEquals(RECIPE_SOURCE, recipe.getSource());
        assertEquals(RECIPE_URL, recipe.getUrl());
        assertEquals(RECIPE_DIRECTIONS, recipe.getDirections());
        assertEquals(NOTES_ID, recipe.getNotes().getId());
        assertEquals(2, recipe.getIngredients().size());
        assertEquals(2, recipe.getCategories().size());
    }
}