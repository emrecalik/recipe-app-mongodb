package com.edoras.recipeproject.services;

import com.edoras.recipeproject.commands.RecipeCommand;
import com.edoras.recipeproject.converters.RecipeCommandToRecipe;
import com.edoras.recipeproject.converters.RecipeToRecipeCommand;
import com.edoras.recipeproject.domains.Recipe;
import com.edoras.recipeproject.exceptions.NotFoundException;
import com.edoras.recipeproject.repositories.RecipeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RecipeServiceImplTest {
    RecipeService recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    RecipeCommandToRecipe recipeCommandConverter;

    @Mock
    RecipeToRecipeCommand recipeConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository, recipeCommandConverter, recipeConverter);
    }

    @Test
    void getRecipes() {
        Recipe recipe = new Recipe();
        List<Recipe> recipesData = new LinkedList<>();
        recipesData.add(recipe);

        when(recipeService.getRecipes()).thenReturn(recipesData);

        List<Recipe> recipes = recipeService.getRecipes();

        assertEquals(recipes.size(),1);

        Mockito.verify(recipeRepository, Mockito.times(1)).findAll();

        recipeService.getRecipes();
    }

    @Test
    void findById() {
        Recipe recipe = new Recipe();
        recipe.setId("1");

        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(recipe));

        Recipe recipeReturned = recipeService.findById("1");

        assertEquals(recipe, recipeReturned);

        verify(recipeRepository, times(1)).findById(anyString());
    }

    @Test()
    void notFoundById() {
        when(recipeRepository.findById(anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> recipeService.findById("1"));
    }

    @Test
    void findCommandById() {
        Recipe recipe = new Recipe();
        recipe.setId("1");

        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(recipe));

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("1");

        when(recipeConverter.convert(any())).thenReturn(recipeCommand);

        RecipeCommand recipeCommandReturned = recipeService.findCommandById("1");

        verify(recipeRepository, times(1)).findById("1");
        verify(recipeConverter, times(1)).convert(recipe);
    }

    @Test
    void deleteById() {
        recipeService.deleteById("1");
        verify(recipeRepository, times(1)).deleteById(anyString());
    }
}