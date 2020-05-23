package com.edoras.recipeproject.domains;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryTest {

    Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
    }

    @Test
    void getId() {
        String idValue = "4";
        category.setId(idValue);
        assertEquals(idValue, category.getId());
    }

    @Test
    void getCategoryName() {
    }

    @Test
    void getRecipes() {
    }
}