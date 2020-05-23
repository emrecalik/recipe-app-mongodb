package com.edoras.recipeproject.repositories.reactive;

import com.edoras.recipeproject.domains.Recipe;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface RecipeReactiveRepository extends ReactiveMongoRepository<Recipe, String> {
}
