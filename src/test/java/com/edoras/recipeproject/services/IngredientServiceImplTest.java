package com.edoras.recipeproject.services;

import com.edoras.recipeproject.commands.IngredientCommand;
import com.edoras.recipeproject.commands.UnitOfMeasureCommand;
import com.edoras.recipeproject.converters.IngredientCommandToIngredient;
import com.edoras.recipeproject.converters.IngredientToIngredientCommand;
import com.edoras.recipeproject.converters.UomCommandToUom;
import com.edoras.recipeproject.converters.UomToUomCommand;
import com.edoras.recipeproject.domains.Ingredient;
import com.edoras.recipeproject.domains.Recipe;
import com.edoras.recipeproject.domains.UnitOfMeasure;
import com.edoras.recipeproject.repositories.RecipeRepository;
import com.edoras.recipeproject.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class IngredientServiceImplTest {

    private final IngredientToIngredientCommand ingredientConverter;
    private final IngredientCommandToIngredient ingredientCommandConverter;

    IngredientServiceImpl ingredientService;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    UnitOfMeasureRepository uomRepository;

    private final Long RECIPE_ID = 1L;
    private final Long INGREDIENT_ID = 2L;

    IngredientServiceImplTest() {
        this.ingredientCommandConverter = new IngredientCommandToIngredient(new UomCommandToUom());
        this.ingredientConverter = new IngredientToIngredientCommand(new UomToUomCommand());
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ingredientService = new IngredientServiceImpl(ingredientConverter, recipeRepository, uomRepository, ingredientCommandConverter);
    }

    @Test
    void findCommandByRecipeIdAndIngredientId() {
        // given
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);
        ingredient1.setRecipe(recipe);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);
        ingredient2.setRecipe(recipe);

        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId(3L);
        ingredient3.setRecipe(recipe);

        Set<Ingredient> ingredientSet = new HashSet<>();
        ingredientSet.add(ingredient1);
        ingredientSet.add(ingredient2);
        ingredientSet.add(ingredient3);

        recipe.setIngredients(ingredientSet);

        when(recipeRepository.findById(anyLong())).thenReturn(java.util.Optional.of(recipe));

        // when
        IngredientCommand ingredientCommand = ingredientService.findCommandByRecipeIdAndIngredientId(1L, 2L);

        // then
        assertEquals(2L, ingredientCommand.getId());
        assertEquals(1L, ingredientCommand.getRecipeId());
        verify(recipeRepository, times(1)).findById(anyLong());
    }

    @Test
    void save() {
        // given
        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(1L);

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(3L);
        ingredientCommand.setRecipeId(2L);
        ingredientCommand.setUnitOfMeasureCommand(unitOfMeasureCommand);

        Recipe recipe = new Recipe();
        recipe.setId(2L);
        Ingredient ingredient = new Ingredient();
        ingredient.setId(3L);
        Set<Ingredient> ingredientSet = new HashSet<>();
        ingredientSet.add(ingredient);
        recipe.setIngredients(ingredientSet);

        when(recipeRepository.findById(anyLong())).thenReturn(java.util.Optional.of(recipe));
        when(recipeRepository.save(any())).thenReturn(recipe);
        when(uomRepository.findById(anyLong())).thenReturn(java.util.Optional.of(new UnitOfMeasure()));

        // when
        ingredientService.save(ingredientCommand);

        // then
        assertEquals(3L, ingredientCommand.getId());
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository, times(1)).save(any());

    }
}