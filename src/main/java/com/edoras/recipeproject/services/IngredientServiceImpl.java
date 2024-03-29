package com.edoras.recipeproject.services;

import com.edoras.recipeproject.commands.IngredientCommand;
import com.edoras.recipeproject.converters.IngredientCommandToIngredient;
import com.edoras.recipeproject.converters.IngredientToIngredientCommand;
import com.edoras.recipeproject.domains.Ingredient;
import com.edoras.recipeproject.domains.Recipe;
import com.edoras.recipeproject.domains.UnitOfMeasure;
import com.edoras.recipeproject.repositories.reactive.RecipeReactiveRepository;
import com.edoras.recipeproject.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    RecipeReactiveRepository recipeReactiveRepository;
    IngredientToIngredientCommand ingredientConverter;
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
    IngredientCommandToIngredient ingredientCommandConverter;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientConverter,
                                 RecipeReactiveRepository recipeReactiveRepository,
                                 UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository,
                                 IngredientCommandToIngredient ingredientCommandConverter) {
        this.ingredientConverter = ingredientConverter;
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
        this.ingredientCommandConverter = ingredientCommandConverter;
    }

    @Override
    public Mono<IngredientCommand> findCommandByRecipeIdAndIngredientId(String recipeId, String ingredientId) {
        return recipeReactiveRepository.findById(recipeId)
                .map(recipe -> recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equalsIgnoreCase(ingredientId))
                .findFirst())
                .filter(Optional::isPresent)
                .map(ingredient -> {
                    IngredientCommand command = ingredientConverter.convert(ingredient.get());
                    command.setRecipeId(recipeId);
                    return command;
                });
    }

    @Override
    public Mono<IngredientCommand> save(IngredientCommand command) {

        Optional<Recipe> recipeOptional = recipeReactiveRepository.findById(command.getRecipeId()).blockOptional();

        if(!recipeOptional.isPresent()){

            // todo toss error if not found!
            log.error("Recipe not found for id: " + command.getRecipeId());
            return Mono.just(new IngredientCommand());
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

                UnitOfMeasure unitOfMeasure
                        = unitOfMeasureReactiveRepository.findById(command.getUnitOfMeasureCommand().getId()).block();
                ingredientFound.setUnitOfMeasure(unitOfMeasure);

            } else {
                //add new Ingredient
                Ingredient ingredient = ingredientCommandConverter.convert(command);
                recipe.addIngredient(ingredient);
            }

            Recipe savedRecipe = recipeReactiveRepository.save(recipe).block();

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

            return Mono.just(ingredientConverter.convert(savedIngredient.get()));
        }
    }

    @Transactional
    @Override
    public Mono<Void> deleteById(String recipeId, String ingredientId) {
        Optional<Recipe> recipeOptional = recipeReactiveRepository.findById(recipeId).blockOptional();

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

        recipe.getIngredients().remove(ingredientToDelete);

        recipeReactiveRepository.save(recipe).block();

        return Mono.empty();
    }
}
