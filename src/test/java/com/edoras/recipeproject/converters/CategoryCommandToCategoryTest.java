package com.edoras.recipeproject.converters;

import com.edoras.recipeproject.commands.CategoryCommand;
import com.edoras.recipeproject.domains.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryCommandToCategoryTest {

    CategoryCommandToCategory categoryCommandConverter;

    private final Long CATEGORY_ID = 1L;
    private final String CATEGORY_NAME = "Mexican";

    @BeforeEach
    void setUp() {
        categoryCommandConverter = new CategoryCommandToCategory();
    }

    @Test
    void testNullSource() {
        assertNull(categoryCommandConverter.convert(null));
    }

    @Test
    void testEmptySource() {
        assertNotNull(categoryCommandConverter.convert(new CategoryCommand()));
    }

    @Test
    void convert() {
        // given
        CategoryCommand categoryCommand = new CategoryCommand();
        categoryCommand.setId(CATEGORY_ID);
        categoryCommand.setDescription(CATEGORY_NAME);

        // when
        Category category = categoryCommandConverter.convert(categoryCommand);

        // then
        assertEquals(CATEGORY_ID, category.getId());
        assertEquals(CATEGORY_NAME, category.getDescription());
    }
}