package com.edoras.recipeproject.services;

import com.edoras.recipeproject.commands.RecipeCommand;
import com.edoras.recipeproject.converters.RecipeCommandToRecipe;
import com.edoras.recipeproject.converters.RecipeToRecipeCommand;
import com.edoras.recipeproject.domains.Recipe;
import com.edoras.recipeproject.exceptions.NotFoundException;
import com.edoras.recipeproject.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandConverter;
    private final RecipeToRecipeCommand recipeConverter;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommandToRecipe recipeCommandConverter,
                             RecipeToRecipeCommand recipeConverter) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandConverter = recipeCommandConverter;
        this.recipeConverter = recipeConverter;
    }

    @Override
    public List<Recipe> getRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();
        return recipes;
    }

    @Override
    public Recipe findById(Long id) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if (recipeOptional.isEmpty()) {
            throw new NotFoundException("Recipe not found for Id: " + id.toString());
        }
        return recipeOptional.get();
    }

    @Override
    public RecipeCommand findCommandById(Long id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        return recipeConverter.convert(recipe.get());
    }

    @Transactional
    @Override
    public RecipeCommand save(RecipeCommand recipeCommand) {
        Recipe recipe = recipeCommandConverter.convert(recipeCommand);
        Recipe savedRecipe = recipeRepository.save(recipe);
        return recipeConverter.convert(savedRecipe);
    }

    @Override
    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }
}
