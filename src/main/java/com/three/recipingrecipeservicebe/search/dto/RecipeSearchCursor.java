package com.three.recipingrecipeservicebe.search.dto;


import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
public class RecipeSearchCursor {
    private final List<Long> recipeIds;
    private final String nextCursor;
    private final int currentCount;
    private final boolean hasNext;
    private final int pageCount;
    private final long totalHits;
    private final long totalPages;


    public RecipeSearchCursor(List<Long> recipeIds, String nextCursor, int currentCount, boolean hasNext, int pageCount, long totalHits, long totalPages) {
        this.recipeIds = recipeIds;
        this.nextCursor = nextCursor;
        this.currentCount = currentCount;
        this.hasNext = hasNext;
        this.pageCount = pageCount;
        this.totalHits = totalHits;
        this.totalPages = totalPages;
    }
}
