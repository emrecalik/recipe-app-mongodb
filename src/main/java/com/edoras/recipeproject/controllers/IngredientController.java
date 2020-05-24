package com.edoras.recipeproject.controllers;

import com.edoras.recipeproject.commands.IngredientCommand;
import com.edoras.recipeproject.services.IngredientService;
import com.edoras.recipeproject.services.RecipeService;
import com.edoras.recipeproject.services.UomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
public class IngredientController {

    private final String RECIPE_INGREDIENT_FORM = "recipe/ingredient/form";

    RecipeService recipeService;

    IngredientService ingredientService;

    UomService uomService;

    public IngredientController(RecipeService recipeService,
                                IngredientService ingredientService,
                                UomService uomService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.uomService = uomService;
    }

    WebDataBinder webDataBinder;

    @InitBinder("ingredient")
    public void initBinder(WebDataBinder webDataBinder) {
        this.webDataBinder = webDataBinder;
    }

    @GetMapping("/recipe/{id}/ingredients")
    public String listIngredients(@PathVariable String id, Model model) {
        model.addAttribute("recipe", recipeService.findById(id));
        return "recipe/ingredient/list";
    }

    @GetMapping("/recipe/{recipeId}/ingredients/{ingredientId}/show")
    public String showIngredient(@PathVariable String recipeId,
                                 @PathVariable String ingredientId, Model model) {
        model.addAttribute("ingredient"
                , ingredientService.findCommandByRecipeIdAndIngredientId(recipeId, ingredientId));
        return "recipe/ingredient/show";
    }

    @GetMapping("/recipe/{recipeId}/ingredients/{ingredientId}/update")
    public String showIngredientForm(@PathVariable String recipeId,
                                     @PathVariable String ingredientId, Model model) {
        model.addAttribute("ingredient"
                , ingredientService.findCommandByRecipeIdAndIngredientId(recipeId, ingredientId));
        model.addAttribute("uomList", uomService.findAll());
        return RECIPE_INGREDIENT_FORM;
    }

    @PostMapping("/recipe/{recipeId}/ingredient")
    public Mono<String> saveOrUpdateIngredient(@ModelAttribute("ingredient") IngredientCommand ingredientCommand,
                                               @PathVariable String recipeId, Model model) {
        webDataBinder.validate();
        BindingResult bindingResult = webDataBinder.getBindingResult();
        model.addAttribute("uomList", uomService.findAll());
        if (bindingResult.hasErrors()) {
            return Mono.just(RECIPE_INGREDIENT_FORM);
        }

        return ingredientService.save(ingredientCommand)
                .map(savedIngredientCommand
                        -> "redirect:/recipe/" + recipeId + "/ingredients/" + savedIngredientCommand.getId() + "/show");
    }

    @GetMapping("/recipe/{recipeId}/ingredient/new")
    public String newIngredientForm(@PathVariable String recipeId, Model model) {
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeId);
        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", uomService.findAll());

        return "recipe/ingredient/form";
    }

    @GetMapping("/recipe/{recipeId}/ingredients/{ingredientId}/delete")
    public String deleteIngredient(@PathVariable String recipeId,
                                   @PathVariable String ingredientId) {
        ingredientService.deleteById(recipeId, ingredientId).block();
        return "redirect:/recipe/" + recipeId + "/ingredients";
    }

}
