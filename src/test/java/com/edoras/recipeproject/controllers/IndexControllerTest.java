package com.edoras.recipeproject.controllers;

import com.edoras.recipeproject.domains.Recipe;
import com.edoras.recipeproject.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class IndexControllerTest {

    @Mock
    RecipeService recipeService;

    @Mock
    Model model;

    IndexController indexController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        indexController = new IndexController(recipeService);
    }

    @Test
    void MockMvcTest() throws Exception {
        Recipe recipe1 = new Recipe();
        recipe1.setDescription("Recipe1");

        Recipe recipe2 = new Recipe();
        recipe2.setDescription("Recipe2");

        List<Recipe> recipeList = new ArrayList<>();
        recipeList.add(recipe1);
        recipeList.add(recipe2);

        Mockito.when(recipeService.getRecipes()).thenReturn(Flux.fromIterable(recipeList));

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void showHome() {
        List<Recipe> recipes = new LinkedList<>();
        recipes.add(new Recipe());
        Recipe anotherRecipe = new Recipe();
        anotherRecipe.setId("2");
        recipes.add(anotherRecipe);
        Mockito.when(recipeService.getRecipes()).thenReturn(Flux.fromIterable(recipes));

        String index = indexController.showHome(model);

        Mockito.verify(recipeService, Mockito.times(1)).getRecipes();

        Mockito.verify(model, Mockito.times(1)).addAttribute("recipes", recipes);

        assertEquals(index, "index");

        assertEquals(recipes.size(),2);
    }
}