package com.edoras.recipeproject.repositories;

import com.edoras.recipeproject.domains.Recipe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, String> {
    Optional<Recipe> findByDescription(String description);
}
