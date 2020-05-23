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
import com.edoras.recipeproject.repositories.reactive.RecipeReactiveRepository;
import com.edoras.recipeproject.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class IngredientServiceImplTest {

    private final IngredientToIngredientCommand ingredientConverter;
    private final IngredientCommandToIngredient ingredientCommandConverter;

    IngredientServiceImpl ingredientService;

    @Mock
    RecipeReactiveRepository recipeReactiveRepository;

    @Mock
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    private final String RECIPE_ID = "1";
    private final String INGREDIENT_ID = "2";

    IngredientServiceImplTest() {
        this.ingredientCommandConverter = new IngredientCommandToIngredient(new UomCommandToUom());
        this.ingredientConverter = new IngredientToIngredientCommand(new UomToUomCommand());
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ingredientService = new IngredientServiceImpl(ingredientConverter,
                recipeReactiveRepository, unitOfMeasureReactiveRepository, ingredientCommandConverter);
    }

    @Test
    void findCommandByRecipeIdAndIngredientId() {
        // given
        Recipe recipe = new Recipe();
        recipe.setId("1");

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId("1");

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId("2");

        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId("3");

        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addIngredient(ingredient3);

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        // when
        IngredientCommand ingredientCommand = ingredientService.findCommandByRecipeIdAndIngredientId("1", "2").block();

        // then
        assertEquals("2", ingredientCommand.getId());
        verify(recipeReactiveRepository, times(1)).findById(anyString());
    }

    @Test
    void save() {
        // given
        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId("1");

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId("3");
        ingredientCommand.setRecipeId("2");
        ingredientCommand.setUnitOfMeasureCommand(unitOfMeasureCommand);

        Recipe recipe = new Recipe();
        recipe.setId("2");
        Ingredient ingredient = new Ingredient();
        ingredient.setId("3");
        Set<Ingredient> ingredientSet = new HashSet<>();
        ingredientSet.add(ingredient);
        recipe.setIngredients(ingredientSet);

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));
        when(recipeReactiveRepository.save(any())).thenReturn(Mono.just(recipe));
        when(unitOfMeasureReactiveRepository.findById(anyString())).thenReturn(Mono.just(new UnitOfMeasure()));

        // when
        ingredientService.save(ingredientCommand);

        // then
        assertEquals("3", ingredientCommand.getId());
        verify(recipeReactiveRepository, times(1)).findById(anyString());
        verify(recipeReactiveRepository, times(1)).save(any());

    }
}