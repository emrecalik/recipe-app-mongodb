package com.edoras.recipeproject.services;

import com.edoras.recipeproject.commands.UnitOfMeasureCommand;
import com.edoras.recipeproject.converters.UomToUomCommand;
import com.edoras.recipeproject.domains.UnitOfMeasure;
import com.edoras.recipeproject.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UomServiceImplTest {

    @Mock
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    UomToUomCommand uomConverter = new UomToUomCommand();

    UomServiceImpl uomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        uomService = new UomServiceImpl(unitOfMeasureReactiveRepository, uomConverter);
    }

    @Test
    void findAll() {
        // given
        UnitOfMeasure uomCommand1 = new UnitOfMeasure();
        uomCommand1.setId("1");

        UnitOfMeasure uomCommand2 = new UnitOfMeasure();
        uomCommand2.setId("2");

        List<UnitOfMeasure> unitOfMeasures = new ArrayList<>();
        unitOfMeasures.add(uomCommand1);
        unitOfMeasures.add(uomCommand2);

        when(unitOfMeasureReactiveRepository.findAll()).thenReturn(Flux.just(uomCommand1, uomCommand2));
        // when
        List<UnitOfMeasureCommand> uomCommandSet = uomService.findAll().collectList().block();

        // then
        assertEquals(2, uomCommandSet.size());
        verify(unitOfMeasureReactiveRepository, times(1)).findAll();
    }
}