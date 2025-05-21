package com.three.recipingrecipeservicebe.recipe.entity.enums;

import com.three.recipingrecipeservicebe.recipe.entity.CookingTime;
import com.three.recipingrecipeservicebe.recipe.entity.Difficulty;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DifficultyConverter implements AttributeConverter<Difficulty, String> {

    @Override
    public String convertToDatabaseColumn(Difficulty attribute) {
        return attribute != null ? attribute.getLabel() : null;
    }

    @Override
    public Difficulty convertToEntityAttribute(String dbData) {
        return dbData != null ? Difficulty.fromLabel(dbData) : null;
    }
}