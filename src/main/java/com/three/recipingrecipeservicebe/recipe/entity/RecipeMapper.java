package com.three.recipingrecipeservicebe.recipe.entity;

import com.three.recipingrecipeservicebe.recipe.dto.RecipeCreatedResponseDto;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeDetailResponseDto;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeSummaryResponseDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecipeMapper {

    RecipeDetailResponseDto toDto(Recipe recipe);

    RecipeSummaryResponseDto toListDto(Recipe recipe);

    RecipeCreatedResponseDto toCreatedDto(Recipe recipe);

    @Mapping(target = "likeCount", constant = "0")
    RecipeSummaryResponseDto toMyRecipeSummaryDto(Recipe recipe);
}
