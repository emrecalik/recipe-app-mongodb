package com.edoras.recipeproject.services;

import com.edoras.recipeproject.commands.IngredientCommand;
import com.edoras.recipeproject.converters.IngredientCommandToIngredient;
import com.edoras.recipeproject.converters.IngredientToIngredientCommand;
import com.edoras.recipeproject.domains.Ingredient;
import com.edoras.recipeproject.domains.Recipe;
import com.edoras.recipeproject.repositories.RecipeRepository;
import com.edoras.recipeproject.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    RecipeRepository recipeRepository;
    IngredientToIngredientCommand ingredientConverter;
    UnitOfMeasureRepository unitOfMeasureRepository;
    IngredientCommandToIngredient ingredientCommandConverter;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientConverter,
                                 RecipeRepository recipeRepository,
                                 UnitOfMeasureRepository unitOfMeasureRepository,
                                 IngredientCommandToIngredient ingredientCommandConverter) {
        this.ingredientConverter = ingredientConverter;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.ingredientCommandConverter = ingredientCommandConverter;
    }

    @Transactional
    @Override
    public IngredientCommand findCommandByRecipeIdAndIngredientId(String recipeId, String ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (!recipeOptional.isPresent()) {
            log.error("recipe id not found: " + recipeId);
        }

        Recipe recipe = recipeOptional.get();

        Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredient -> ingredientConverter.convert(ingredient)).findFirst();

        if (!ingredientCommandOptional.isPresent()) {
            log.error("ingredient id not found: " + ingredientId);
        }

        return ingredientCommandOptional.get();
    }

    @Transactional
    @Override
    public IngredientCommand save(IngredientCommand command) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

        if(!recipeOptional.isPresent()){

            //todo toss error if not found!
            log.error("Recipe not found for id: " + command.getRecipeId());
            return new IngredientCommand();
        } else {
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(command.getId()))
                    .findFirst();

            if(ingredientOptional.isPresent()){
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(command.getDescription());
                ingredientFound.setAmount(command.getAmount());
                ingredientFound.setUnitOfMeasure(unitOfMeasureRepository
                        .findById(command.getUnitOfMeasureCommand().getId())
                        .orElseThrow(() -> new RuntimeException("UOM NOT FOUND"))); //todo address this
            } else {
                //add new Ingredient
                Ingredient ingredient = ingredientCommandConverter.convert(command);
                ingredient.setRecipe(recipe);
                recipe.addIngredients(ingredient);
            }

            Recipe savedRecipe = recipeRepository.save(recipe);

            Optional<Ingredient> savedIngredient = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredient -> recipeIngredient.getId().equals(command.getId()))
                    .findFirst();

            if (!savedIngredient.isPresent()) {
                savedIngredient = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredient -> recipeIngredient.getDescription().equals(command.getDescription()))
                        .filter(recipeIngredient -> recipeIngredient.getAmount().equals(command.getAmount()))
                        .filter(recipeIngredient -> recipeIngredient.getUnitOfMeasure().getId().equals(command.getUnitOfMeasureCommand().getId()))
                        .findFirst();
            }

            //to do check for fail
            return ingredientConverter.convert(savedIngredient.get());
        }
    }

    @Transactional
    @Override
    public void deleteById(String recipeId, String ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (!recipeOptional.isPresent()) {
            log.error("Recipe not found for id: " + recipeId);
        }

        Recipe recipe = recipeOptional.get();

        Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .findFirst();

        if (!recipeOptional.isPresent()) {
            log.error("Ingredient not found for id: " + ingredientId);
        }

        Ingredient ingredientToDelete = ingredientOptional.get();
        ingredientToDelete.setRecipe(null);

        recipe.getIngredients().remove(ingredientToDelete);

        recipeRepository.save(recipe);
    }
}
