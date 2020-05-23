package com.edoras.recipeproject.services;

import com.edoras.recipeproject.commands.UnitOfMeasureCommand;
import reactor.core.publisher.Flux;

public interface UomService {
    Flux<UnitOfMeasureCommand> findAll();
}
