package com.edoras.recipeproject.converters;

import com.edoras.recipeproject.commands.CategoryCommand;
import com.edoras.recipeproject.domains.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryToCategoryCommandTest {

    CategoryToCategoryCommand categoryConverter;

    private final long CATEGORY_ID = 1L;
    private final String CATEGORY_NAME = "Turkish";

    @BeforeEach
    void setUp() {
        categoryConverter = new CategoryToCategoryCommand();
    }

    @Test
    void testNullSource() {
        assertNull(categoryConverter.convert(null));
    }

    @Test
    void testEmptySource() {
        assertNotNull(categoryConverter.convert(new Category()));
    }

    @Test
    void convert() {
        // given
        Category category = new Category();
        category.setId(CATEGORY_ID);
        category.setDescription(CATEGORY_NAME);

        // when
        CategoryCommand categoryCommand = categoryConverter.convert(category);

        // then
        assertEquals(CATEGORY_ID, categoryCommand.getId());
        assertEquals(CATEGORY_NAME, categoryCommand.getDescription());
    }
}