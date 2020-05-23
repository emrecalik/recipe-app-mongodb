package com.edoras.recipeproject.services;

import com.edoras.recipeproject.commands.UnitOfMeasureCommand;
import com.edoras.recipeproject.converters.UomToUomCommand;
import com.edoras.recipeproject.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class UomServiceImpl implements UomService {

    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
    UomToUomCommand uomConverter;

    public UomServiceImpl(UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository,
                          UomToUomCommand uomConverter) {
        this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
        this.uomConverter = uomConverter;
    }

    @Override
    public Flux<UnitOfMeasureCommand> findAll() {
        Flux<UnitOfMeasureCommand> uomSet = unitOfMeasureReactiveRepository.findAll()
                .map(item -> uomConverter.convert(item));
        return uomSet;
    }
}
