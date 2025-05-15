package com.three.recipingrecipeservicebe.recipe.entity.enums;

import com.three.recipingrecipeservicebe.recipe.entity.CookingTime;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CookingTimeConverter implements AttributeConverter<CookingTime, String> {

    @Override
    public String convertToDatabaseColumn(CookingTime attribute) {
        return attribute != null ? attribute.getLabel() : null;
    }

    @Override
    public CookingTime convertToEntityAttribute(String dbData) {
        return dbData != null ? CookingTime.fromLabel(dbData) : null;
    }
}
