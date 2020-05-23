package com.edoras.recipeproject.repositories.reactive;

import com.edoras.recipeproject.domains.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UnitOfMeasureReactiveRepositoryTest {

    public static final String TOO_MANY = "TOO_MANY";
    @Autowired
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    @BeforeEach
    void setUp() {
        unitOfMeasureReactiveRepository.deleteAll().block();
    }

    @Test
    void testSave() {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setDescription(TOO_MANY);

        unitOfMeasureReactiveRepository.save(unitOfMeasure).block();

        assertEquals(1, unitOfMeasureReactiveRepository.findAll().count().block());
    }

    @Test
    void testFindByDescription() {
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setDescription(TOO_MANY);

        unitOfMeasureReactiveRepository.save(unitOfMeasure).block();

        UnitOfMeasure savedUnitOfMeasure = unitOfMeasureReactiveRepository.findByDescription(TOO_MANY).block();

        assertEquals(TOO_MANY, savedUnitOfMeasure.getDescription());
    }
}