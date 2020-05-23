package com.edoras.recipeproject.repositories;

import com.edoras.recipeproject.domains.Recipe;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RecipeRepository extends CrudRepository<Recipe, String> {
    Optional<Recipe> findByDescription(String description);
}
