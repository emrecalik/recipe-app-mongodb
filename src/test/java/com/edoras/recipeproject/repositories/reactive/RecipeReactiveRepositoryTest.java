package com.edoras.recipeproject.repositories.reactive;

import com.edoras.recipeproject.domains.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RecipeReactiveRepositoryTest {

    public static final String KEBAP = "Kebap";
    @Autowired
    RecipeReactiveRepository recipeReactiveRepository;

    @BeforeEach
    void setUp() {
        recipeReactiveRepository.deleteAll().block();
    }


    @Test
    void testSave() {
        Recipe recipe = new Recipe();
        recipe.setDescription(KEBAP);

        recipeReactiveRepository.save(recipe).block();

        assertEquals(1, recipeReactiveRepository.findAll().count().block());
    }
}