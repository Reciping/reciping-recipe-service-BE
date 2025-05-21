package com.three.recipingrecipeservicebe.search.dto;

import com.three.recipingrecipeservicebe.recipe.entity.Recipe;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RecipeSearchCursorResponse {
    private final List<Recipe> recipes;
    private final String nextCursor;
    private final int currentCount;
    private final boolean hasNext;
    private final int pageCount;
    private final long totalHits;
    private final long totalPages;
}
