package com.edoras.recipeproject.services;

import com.edoras.recipeproject.commands.RecipeCommand;
import com.edoras.recipeproject.converters.RecipeCommandToRecipe;
import com.edoras.recipeproject.converters.RecipeToRecipeCommand;
import com.edoras.recipeproject.domains.Recipe;
import com.edoras.recipeproject.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeReactiveRepository recipeReactiveRepository;
    private final RecipeCommandToRecipe recipeCommandConverter;
    private final RecipeToRecipeCommand recipeConverter;

    @Autowired
    public RecipeServiceImpl(RecipeReactiveRepository recipeReactiveRepository, RecipeCommandToRecipe recipeCommandConverter,
                             RecipeToRecipeCommand recipeConverter) {
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.recipeCommandConverter = recipeCommandConverter;
        this.recipeConverter = recipeConverter;
    }

    @Override
    public Flux<Recipe> getRecipes() {

        return recipeReactiveRepository.findAll();
    }

    @Override
    public Mono<Recipe> findById(String id) {
        return recipeReactiveRepository.findById(id);
    }

    @Override
    public Mono<RecipeCommand> findCommandById(String id) {
        return recipeReactiveRepository.findById(id).map(recipe -> {
            RecipeCommand recipeCommand = recipeConverter.convert(recipe);
            return recipeCommand;
        });
    }

    @Override
    public Mono<RecipeCommand> save(RecipeCommand recipeCommand) {
//        Recipe recipe = recipeCommandConverter.convert(recipeCommand);
//        Recipe savedRecipe = recipeReactiveRepository.save(recipe).block();
//        RecipeCommand savedRecipeCommand = recipeConverter.convert(savedRecipe);

        return recipeReactiveRepository.save(recipeCommandConverter.convert(recipeCommand))
                .map(savedRecipe -> recipeConverter.convert(savedRecipe));

//        return Mono.just(savedRecipeCommand);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        recipeReactiveRepository.deleteById(id);
        return Mono.empty();
    }
}
