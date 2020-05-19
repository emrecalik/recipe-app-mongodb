package com.edoras.recipeproject.converters;

import com.edoras.recipeproject.commands.RecipeCommand;
import com.edoras.recipeproject.domains.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RecipeToRecipeCommandTest {

    RecipeToRecipeCommand recipeConverter;

    private final Long RECIPE_ID = 1L;
    private final String RECIPE_DESCRIPTION = "my recipe";
    private final int RECIPE_PREPTIME = 7;
    private final int RECIPE_COOKTIME = 5;
    private final int RECIPE_SERVING = 3;
    private final String RECIPE_SOURCE = "source";
    private final String RECIPE_URL = "some url";
    private final String RECIPE_DIRECTIONS = "directions";
    private final Difficulty RECIPE_DIFFICULTY = Difficulty.MODERATE;

    private final Long NOTES_ID = 2L;
    private final Long INGREDIENT_ID_1 = 3L;
    private final Long INGREDIENT_ID_2 = 4L;
    private final Long CATEGORY_ID_1 = 2L;
    private final Long CATEGORY_ID_2 = 3L;

    @BeforeEach
    void setUp() {
        recipeConverter = new RecipeToRecipeCommand(new NotesToNotesCommand(),
                new IngredientToIngredientCommand(new UomToUomCommand()),
                new CategoryToCategoryCommand());
    }

    @Test
    void testNullSource() {
        assertNull(recipeConverter.convert(null));
    }

    @Test
    void testEmptySource() {
        assertNotNull(recipeConverter.convert(new Recipe()));
    }

    @Test
    void convert() {
        // given
        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        recipe.setDescription(RECIPE_DESCRIPTION);
        recipe.setPrepTime(RECIPE_PREPTIME);
        recipe.setCookTime(RECIPE_COOKTIME);
        recipe.setServings(RECIPE_SERVING);
        recipe.setSource(RECIPE_SOURCE);
        recipe.setUrl(RECIPE_URL);
        recipe.setDirections(RECIPE_DIRECTIONS);

        Notes notes = new Notes();
        notes.setId(NOTES_ID);

        recipe.setNotes(notes);

        Set<Ingredient> ingredients = new HashSet<>();
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(INGREDIENT_ID_1);
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(INGREDIENT_ID_2);
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);

        recipe.setIngredients(ingredients);

        Set<Category> categories = new HashSet<>();
        Category category1 = new Category();
        category1.setId(CATEGORY_ID_1);
        Category category2 = new Category();
        category2.setId(CATEGORY_ID_2);
        categories.add(category1);
        categories.add(category2);

        recipe.setCategories(categories);
        recipe.setDifficulty(RECIPE_DIFFICULTY);

        // when
        RecipeCommand recipeCommand = recipeConverter.convert(recipe);

        // then
        assertEquals(RECIPE_ID, recipeCommand.getId());
        assertEquals(RECIPE_DESCRIPTION, recipeCommand.getDescription());
        assertEquals(RECIPE_PREPTIME, recipeCommand.getPrepTime());
        assertEquals(RECIPE_COOKTIME, recipeCommand.getCookTime());
        assertEquals(RECIPE_SERVING, recipeCommand.getServings());
        assertEquals(RECIPE_SOURCE, recipeCommand.getSource());
        assertEquals(RECIPE_URL, recipeCommand.getUrl());
        assertEquals(RECIPE_DIRECTIONS, recipeCommand.getDirections());
        assertEquals(NOTES_ID, recipeCommand.getNotesCommand().getId());
        assertEquals(2, recipeCommand.getIngredientCommands().size());
        assertEquals(2, recipeCommand.getCategoryCommands().size());
    }
}