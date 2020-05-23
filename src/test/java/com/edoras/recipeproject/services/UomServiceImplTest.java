package com.edoras.recipeproject.services;

import com.edoras.recipeproject.commands.UnitOfMeasureCommand;
import com.edoras.recipeproject.converters.UomToUomCommand;
import com.edoras.recipeproject.domains.UnitOfMeasure;
import com.edoras.recipeproject.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UomServiceImplTest {

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    UomToUomCommand uomConverter = new UomToUomCommand();

    UomServiceImpl uomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        uomService = new UomServiceImpl(unitOfMeasureRepository, uomConverter);
    }

    @Test
    void findAll() {
        // given
        UnitOfMeasure uomCommand1 = new UnitOfMeasure();
        uomCommand1.setId("1");

        UnitOfMeasure uomCommand2 = new UnitOfMeasure();
        uomCommand2.setId("2");

        Set<UnitOfMeasure> unitOfMeasures = new HashSet<>();
        unitOfMeasures.add(uomCommand1);
        unitOfMeasures.add(uomCommand2);

        when(unitOfMeasureRepository.findAll()).thenReturn(unitOfMeasures);
        // when
        Set<UnitOfMeasureCommand> uomCommandSet = uomService.findAll();

        // then
        assertEquals(2, uomCommandSet.size());
        verify(unitOfMeasureRepository, times(1)).findAll();
    }
}