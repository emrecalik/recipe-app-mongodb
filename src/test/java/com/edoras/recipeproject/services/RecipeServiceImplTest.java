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
import static org.mockito.ArgumentMatchers.anyLong;
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
        List<Recipe> recipes = new LinkedList<>();
        recipes.add(recipe);

        when(recipeRepository.findAll()).thenReturn(recipes);

        assertEquals(recipeRepository.findAll().size(),1);

        Mockito.verify(recipeRepository, Mockito.times(1)).findAll();

        recipeService.getRecipes();
    }

    @Test
    void findById() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));

        Recipe recipeReturned = recipeService.findById(1L);

        assertEquals(recipe, recipeReturned);

        verify(recipeRepository, times(1)).findById(anyLong());
    }

    @Test()
    void notFoundById() {
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> recipeService.findById(1L));
    }

    @Test
    void findCommandById() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        when(recipeConverter.convert(any())).thenReturn(recipeCommand);

        RecipeCommand recipeCommandReturned = recipeService.findCommandById(1L);

        verify(recipeRepository, times(1)).findById(1L);
        verify(recipeConverter, times(1)).convert(recipe);
    }

    @Test
    void deleteById() {
        recipeService.deleteById(1L);
        verify(recipeRepository, times(1)).deleteById(anyLong());
    }
}