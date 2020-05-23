package com.edoras.recipeproject.services;

import com.edoras.recipeproject.commands.RecipeCommand;
import com.edoras.recipeproject.converters.RecipeCommandToRecipe;
import com.edoras.recipeproject.converters.RecipeToRecipeCommand;
import com.edoras.recipeproject.domains.Recipe;
import com.edoras.recipeproject.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RecipeServiceImplTest {
    RecipeService recipeService;

    @Mock
    RecipeReactiveRepository recipeReactiveRepository;

    @Mock
    RecipeCommandToRecipe recipeCommandConverter;

    @Mock
    RecipeToRecipeCommand recipeConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        recipeService = new RecipeServiceImpl(recipeReactiveRepository, recipeCommandConverter, recipeConverter);
    }

    @Test
    void getRecipes() {
        Recipe recipe = new Recipe();
        List<Recipe> recipesData = new LinkedList<>();
        recipesData.add(recipe);

        when(recipeService.getRecipes()).thenReturn(Flux.fromIterable(recipesData));

        List<Recipe> recipes = recipeService.getRecipes().collectList().block();

        assertEquals(recipes.size(),1);

        Mockito.verify(recipeReactiveRepository, Mockito.times(1)).findAll();

        recipeService.getRecipes();
    }

    @Test
    void findById() {
        Recipe recipe = new Recipe();
        recipe.setId("1");

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        Recipe recipeReturned = recipeService.findById("1").block();

        assertEquals(recipe, recipeReturned);

        verify(recipeReactiveRepository, times(1)).findById(anyString());
    }

    @Test
    void findCommandById() {
        Recipe recipe = new Recipe();
        recipe.setId("1");

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("1");

        when(recipeConverter.convert(any())).thenReturn(recipeCommand);

        RecipeCommand recipeCommandReturned = recipeService.findCommandById("1").block();

        verify(recipeReactiveRepository, times(1)).findById("1");
        verify(recipeConverter, times(1)).convert(recipe);
    }

    @Test
    void deleteById() {
        recipeService.deleteById("1");
        verify(recipeReactiveRepository, times(1)).deleteById(anyString());
    }
}