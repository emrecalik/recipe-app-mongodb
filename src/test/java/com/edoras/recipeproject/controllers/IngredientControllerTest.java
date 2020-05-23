package com.edoras.recipeproject.controllers;

import com.edoras.recipeproject.commands.IngredientCommand;
import com.edoras.recipeproject.commands.UnitOfMeasureCommand;
import com.edoras.recipeproject.domains.Recipe;
import com.edoras.recipeproject.services.IngredientService;
import com.edoras.recipeproject.services.RecipeService;
import com.edoras.recipeproject.services.UomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class IngredientControllerTest {

    @Mock
    RecipeService recipeService;

    @Mock
    IngredientService ingredientService;

    @Mock
    UomService uomService;

    IngredientController ingredientController;

    MockMvc mockMvc;

    private final String RECIPE_ID = "1";
    private final String INGREDIENT_ID = "2";

    @BeforeEach
    void setUp() {
        ingredientController = new IngredientController(recipeService, ingredientService, uomService);
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();
    }

    @Test
    void listIngredients() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);

        when(recipeService.findById(anyString())).thenReturn(recipe);

        mockMvc.perform(get("/recipe/" + RECIPE_ID + "/ingredients"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/list"))
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService, times(1)).findById(anyString());
    }

    @Test
    void showIngredient() throws Exception {
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(INGREDIENT_ID);

        when(ingredientService.findCommandByRecipeIdAndIngredientId(anyString(), anyString())).thenReturn(Mono.just(ingredientCommand));

        mockMvc.perform(get("/recipe/" + RECIPE_ID + "/ingredients/" + INGREDIENT_ID + "/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/show"))
                .andExpect(model().attributeExists("ingredient"));
    }

    @Test
    void showIngredientForm() throws Exception {
        // given
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(INGREDIENT_ID);

        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setUnitOfMeasure("HANDFULL");

        // when
        when(ingredientService.findCommandByRecipeIdAndIngredientId(anyString(), anyString())).thenReturn(Mono.just(ingredientCommand));
        when(uomService.findAll()).thenReturn(Flux.just(unitOfMeasureCommand));

        // then
        mockMvc.perform(get("/recipe/" + RECIPE_ID + "/ingredients/" + INGREDIENT_ID + "/update"))
                .andExpect(view().name("recipe/ingredient/form"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));
    }

    @Test
    void saveOrUpdateIngredient() throws Exception {
        // given
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(INGREDIENT_ID);
        ingredientCommand.setRecipeId(RECIPE_ID);

        // when
        when(ingredientService.save(any())).thenReturn(Mono.just(ingredientCommand));

        // then
        mockMvc.perform(post("/recipe/" + RECIPE_ID + "/ingredient"))
                .andExpect(view().name("redirect:/recipe/" + RECIPE_ID + "/ingredients/" + INGREDIENT_ID + "/show"))
                .andExpect(status().is3xxRedirection());

    }

    @Test
    void newIngredientForm() throws Exception {
        // given
        UnitOfMeasureCommand uomCommand = new UnitOfMeasureCommand();
        uomCommand.setId("10");

        when(uomService.findAll()).thenReturn(Flux.just(uomCommand));

        // then
        mockMvc.perform(get("/recipe/" + RECIPE_ID + "/ingredient/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/form"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));
        verify(uomService, times(1)).findAll();
    }

    @Test
    void deleteIngredient() throws Exception {
         mockMvc.perform(get("/recipe/" + RECIPE_ID + "/ingredients/" + INGREDIENT_ID + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/" + RECIPE_ID + "/ingredients"));

        verify(ingredientService, times(1)).deleteById(anyString(), anyString());
    }
}