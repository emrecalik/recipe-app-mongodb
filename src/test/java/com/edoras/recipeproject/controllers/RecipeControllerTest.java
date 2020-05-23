package com.edoras.recipeproject.controllers;

import com.edoras.recipeproject.commands.RecipeCommand;
import com.edoras.recipeproject.domains.Recipe;
import com.edoras.recipeproject.exceptions.NotFoundException;
import com.edoras.recipeproject.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {

    @Mock
    RecipeService recipeService;

    @InjectMocks
    RecipeController recipeController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        recipeController = new RecipeController(recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
                .setControllerAdvice(new GlobalControllerExceptionHandler())
                .build();
    }

    @Test
    void showRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId("1");
        when(recipeService.findById(anyString())).thenReturn(Mono.just(recipe));

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(view().name("recipe/show"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService, times(1)).findById("1");
    }

    @Test
    void handleNotFoundException() throws Exception {
        when(recipeService.findById(anyString())).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/recipe/4/show"))
                .andExpect(status().isNotFound());
    }


    @Test
    void showRecipeForm() throws Exception {
        mockMvc.perform(get("/recipe/new"))
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    void createRecipe() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("3");

        when(recipeService.save(any())).thenReturn(Mono.just(recipeCommand));

        RecipeCommand savedRecipeCommand = recipeService.save(recipeCommand).block();

        assertEquals("3", savedRecipeCommand.getId());

        mockMvc.perform(post("/recipe")
                .param("id", "")
                .param("description", "dummy")
                .param("directions", "dummy")
                .param("prepTime", "10")
                .param("cookTime", "10")
                .param("servings", "10"))
                .andExpect(view().name("redirect:/recipe/3/show"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testFailedValidation() throws Exception {
        mockMvc.perform(post("/recipe"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    void updateRecipe() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("1");

        when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(recipeCommand));

        mockMvc.perform(get("/recipe/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    void deleteRecipe() throws Exception {
        mockMvc.perform(get("/recipe/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(recipeService, times(1)).deleteById(anyString());
    }

    @Test
    void handleNotFoundRecipe() throws Exception {
        when(recipeService.findById(anyString())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipe/4/show"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
    }
}