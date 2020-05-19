package com.edoras.recipeproject.services;

import com.edoras.recipeproject.commands.UnitOfMeasureCommand;
import com.edoras.recipeproject.converters.UomToUomCommand;
import com.edoras.recipeproject.repositories.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UomServiceImpl implements UomService {

    UnitOfMeasureRepository unitOfMeasureRepository;
    UomToUomCommand uomConverter;

    public UomServiceImpl(UnitOfMeasureRepository unitOfMeasureRepository,
                          UomToUomCommand uomConverter) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.uomConverter = uomConverter;
    }

    @Override
    public Set<UnitOfMeasureCommand> findAll() {
        Set<UnitOfMeasureCommand> uomSet = new HashSet<>();
        unitOfMeasureRepository.findAll().forEach(unitOfMeasure -> uomSet.add(uomConverter.convert(unitOfMeasure)));
        return uomSet;
    }
}
