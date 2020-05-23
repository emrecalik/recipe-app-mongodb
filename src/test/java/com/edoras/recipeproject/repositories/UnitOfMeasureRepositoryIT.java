package com.edoras.recipeproject.repositories;

import com.edoras.recipeproject.bootstrap.BootstrapMongoDB;
import com.edoras.recipeproject.domains.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UnitOfMeasureRepositoryIT {

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    @BeforeEach
    void setUp() {
        recipeRepository.deleteAll();
        unitOfMeasureRepository.deleteAll();
        categoryRepository.deleteAll();

        BootstrapMongoDB bootstrapMongoDB = new BootstrapMongoDB(categoryRepository, unitOfMeasureRepository, recipeRepository);

        bootstrapMongoDB.onApplicationEvent(null);
    }

    @Test
    void findByUom() {
        Optional<UnitOfMeasure> savedUnitOfMeasure = unitOfMeasureRepository.findByDescription("Tablespoon");
        assertEquals(savedUnitOfMeasure.get().getDescription(), "Tablespoon");
    }

    @Test
    void findByUomCup() {
        Optional<UnitOfMeasure> unitOfMeasure = unitOfMeasureRepository.findByDescription("Cup");
        assertEquals(unitOfMeasure.get().getDescription(), "Cup");
    }
}