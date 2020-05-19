package com.edoras.recipeproject.controllers;

import com.edoras.recipeproject.commands.IngredientCommand;
import com.edoras.recipeproject.commands.UnitOfMeasureCommand;
import com.edoras.recipeproject.domains.Recipe;
import com.edoras.recipeproject.services.IngredientService;
import com.edoras.recipeproject.services.RecipeService;
import com.edoras.recipeproject.services.UomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Set;

@Controller
public class IngredientController {

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

    @GetMapping("/recipe/{id}/ingredients")
    public String listIngredients(@PathVariable String id, Model model) {
        Recipe recipe = recipeService.findById(id);
        model.addAttribute("recipe", recipe);
        return "recipe/ingredient/list";
    }

    @GetMapping("/recipe/{recipeId}/ingredients/{ingredientId}/show")
    public String showIngredient(@PathVariable String recipeId,
                                 @PathVariable String ingredientId, Model model) {
        IngredientCommand ingredientCommand = ingredientService.findCommandByRecipeIdAndIngredientId(recipeId, ingredientId);
        model.addAttribute("ingredient", ingredientCommand);
        return "recipe/ingredient/show";
    }

    @GetMapping("/recipe/{recipeId}/ingredients/{ingredientId}/update")
    public String showIngredientForm(@PathVariable String recipeId,
                                     @PathVariable String ingredientId, Model model) {
        IngredientCommand ingredientCommand = ingredientService.findCommandByRecipeIdAndIngredientId(recipeId, ingredientId);
        Set<UnitOfMeasureCommand> uomSet = uomService.findAll();
        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", uomSet);
        return "recipe/ingredient/form";
    }

    @PostMapping("/recipe/{recipeId}/ingredient")
    public String saveOrUpdateIngredient(@ModelAttribute IngredientCommand ingredientCommand,
                                         @PathVariable Long recipeId) {
        IngredientCommand savedIngredientCommand = ingredientService.save(ingredientCommand);
        return "redirect:/recipe/" + recipeId + "/ingredients/" + savedIngredientCommand.getId() + "/show";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/new")
    public String newIngredientForm(@PathVariable String recipeId, Model model) {
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeId);
        model.addAttribute("ingredient", ingredientCommand);

        Set<UnitOfMeasureCommand> uomSet = uomService.findAll();
        model.addAttribute("uomList", uomSet);

        return "recipe/ingredient/form";
    }

    @GetMapping("/recipe/{recipeId}/ingredients/{ingredientId}/delete")
    public String deleteIngredient(@PathVariable String recipeId,
                                   @PathVariable String ingredientId) {
        ingredientService.deleteById(recipeId, ingredientId);
        return "redirect:/recipe/" + recipeId + "/ingredients";
    }

}
