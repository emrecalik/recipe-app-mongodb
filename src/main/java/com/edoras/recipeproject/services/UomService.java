package com.edoras.recipeproject.services;

import com.edoras.recipeproject.commands.UnitOfMeasureCommand;

import java.util.Set;

public interface UomService {
    Set<UnitOfMeasureCommand> findAll();
}
