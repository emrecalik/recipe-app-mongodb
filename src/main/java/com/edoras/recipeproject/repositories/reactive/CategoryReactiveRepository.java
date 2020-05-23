package com.edoras.recipeproject.repositories.reactive;

import com.edoras.recipeproject.domains.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoryReactiveRepository extends ReactiveMongoRepository<Category, String> {
}
