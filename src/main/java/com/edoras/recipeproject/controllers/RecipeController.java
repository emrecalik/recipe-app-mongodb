package com.edoras.recipeproject.controllers;

import com.edoras.recipeproject.commands.RecipeCommand;
import com.edoras.recipeproject.exceptions.NotFoundException;
import com.edoras.recipeproject.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class RecipeController {

    private final String RECIPE_FORM_VIEW = "recipe/recipeform";

    RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    WebDataBinder webDataBinder;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        this.webDataBinder = webDataBinder;
    }

    @GetMapping("/recipe/{id}/show")
    public String showRecipe(@PathVariable String id, Model model) {
        model.addAttribute("recipe", recipeService.findById(id));
        return "recipe/show";
    }

    @GetMapping("/recipe/new")
    public String showRecipeForm(Model model) {
        model.addAttribute("recipe", new RecipeCommand());
        return RECIPE_FORM_VIEW;
    }

    @PostMapping("/recipe")
    public Mono<String> createRecipe(@ModelAttribute("recipe") RecipeCommand recipeCommand) {
        webDataBinder.validate();
        BindingResult bindingResult = webDataBinder.getBindingResult();

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> {
                log.debug(objectError.toString());
            });
            return Mono.just(RECIPE_FORM_VIEW);
        }

        Mono<String> redirect = recipeService.save(recipeCommand)
                .map(savedRecipe -> "redirect:/recipe/" + savedRecipe.getId() + "/show");

        return redirect;
    }

    @GetMapping("/recipe/{id}/update")
    public Mono<String> updateRecipe(@PathVariable String id, Model model) {
        model.addAttribute("recipe", recipeService.findCommandById(id));
        return Mono.just(RECIPE_FORM_VIEW);
    }

    @GetMapping("/recipe/{id}/delete")
    public String deleteRecipeById(@PathVariable String id) {
        recipeService.deleteById(id);
        return "redirect:/";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(Exception exception, Model model) {
        model.addAttribute(exception);
        return "404error";
    }
}
