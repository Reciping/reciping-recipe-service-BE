package com.three.recipingrecipeservicebe.recipe.entity;

import com.three.recipingrecipeservicebe.recipe.dto.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecipeMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(RecipeRequestDto dto, @MappingTarget Recipe entity);

    RecipeDetailResponseDto toDto(Recipe recipe);

    RecipeSummaryResponseDto toListDto(Recipe recipe);

    RecipeCreatedResponseDto toCreatedDto(Recipe recipe);

    @Mapping(target = "likeCount", constant = "0")
    RecipeSummaryResponseDto toMyRecipeSummaryDto(Recipe recipe);
}
