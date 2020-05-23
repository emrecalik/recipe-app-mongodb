package com.edoras.recipeproject.repositories.reactive;

import com.edoras.recipeproject.domains.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CategoryReactiveRepositoryTest {

    public static final String TURKISH = "Turkish";
    @Autowired
    CategoryReactiveRepository categoryReactiveRepository;

    @BeforeEach
    void setUp() {
        categoryReactiveRepository.deleteAll().block();
    }

    @Test
    void testSave() {
        Category category = new Category();
        category.setDescription(TURKISH);

        categoryReactiveRepository.save(category).block();

        assertEquals(1, categoryReactiveRepository.findAll().count().block());
    }

    @Test
    void testFindByDescription() {
        Category category = new Category();
        category.setDescription(TURKISH);

        categoryReactiveRepository.save(category).block();

        Category turkishCategory = categoryReactiveRepository.findByDescription(TURKISH).block();

        assertEquals(TURKISH, turkishCategory.getDescription());
    }

}