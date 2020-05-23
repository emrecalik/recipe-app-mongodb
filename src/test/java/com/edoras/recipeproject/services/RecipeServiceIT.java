package com.edoras.recipeproject.services;

import com.edoras.recipeproject.commands.RecipeCommand;
import com.edoras.recipeproject.converters.RecipeToRecipeCommand;
import com.edoras.recipeproject.domains.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RecipeServiceIT {

    @Autowired
    RecipeServiceImpl recipeServiceImpl;

    @Autowired
    RecipeToRecipeCommand recipeConverter;

    private final String NEW_DESCRIPTION = "new description";

    @BeforeEach
    void setUp() { }

    @Test
    void save() {
        // given
        List<Recipe> recipes = recipeServiceImpl.getRecipes().collectList().block();
        Recipe recipe = recipes.get(0);
        recipe.setDescription(NEW_DESCRIPTION);

        // when
        RecipeCommand recipeCommand = recipeConverter.convert(recipe);
        RecipeCommand savedRecipeCommand = recipeServiceImpl.save(recipeCommand).block();

        // then
        assertEquals(NEW_DESCRIPTION, savedRecipeCommand.getDescription());
        assertEquals(recipe.getId(), savedRecipeCommand.getId());
        assertEquals(recipe.getIngredients().size(), savedRecipeCommand.getIngredientCommands().size());
        assertEquals(recipe.getCategories().size(), savedRecipeCommand.getCategoryCommands().size());
    }

}