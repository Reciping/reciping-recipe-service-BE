package com.three.recipingrecipeservicebe.search.service;

import com.three.recipingrecipeservicebe.recipe.entity.Recipe;
import com.three.recipingrecipeservicebe.recipe.repository.RecipeRepository;
import com.three.recipingrecipeservicebe.search.dto.RecipeSearchCursor;
import com.three.recipingrecipeservicebe.search.dto.RecipeSearchCursorResponse;
import com.three.recipingrecipeservicebe.search.feign.SearchFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchFeignClient searchFeignClient;
    private final RecipeRepository recipeRepository;

    public RecipeSearchCursorResponse searchByMenu(String keyword, int size, String cursor, int pageCount) {
        RecipeSearchCursor searchMenuRecipeList = searchFeignClient.searchByMenu(keyword, size, cursor, pageCount);

        List<Recipe> recipes = recipeRepository.findByIdInAndIsDeletedFalse(searchMenuRecipeList.getRecipeIds());

        return RecipeSearchCursorResponse.builder()
                .recipes(recipes)
                .nextCursor(searchMenuRecipeList.getNextCursor())
                .currentCount(searchMenuRecipeList.getCurrentCount())
                .hasNext(searchMenuRecipeList.isHasNext())
                .pageCount(searchMenuRecipeList.getPageCount())
                .totalHits(searchMenuRecipeList.getTotalHits())
                .totalPages(searchMenuRecipeList.getTotalPages())
                .build();
    }

    public RecipeSearchCursorResponse searchByIngredients(List<String> ingredients, int size, String cursor, int pageCount) {
        RecipeSearchCursor searchIngredientsRecipeList = searchFeignClient.searchByIngredients(ingredients, size, cursor, pageCount);

        List<Recipe> recipes = recipeRepository.findByIdInAndIsDeletedFalse(searchIngredientsRecipeList.getRecipeIds());

        return RecipeSearchCursorResponse.builder()
                .recipes(recipes)
                .nextCursor(searchIngredientsRecipeList.getNextCursor())
                .currentCount(searchIngredientsRecipeList.getCurrentCount())
                .hasNext(searchIngredientsRecipeList.isHasNext())
                .pageCount(searchIngredientsRecipeList.getPageCount())
                .totalHits(searchIngredientsRecipeList.getTotalHits())
                .totalPages(searchIngredientsRecipeList.getTotalPages())
                .build();
    }

    public RecipeSearchCursorResponse searchByNatural(String query, int size, String cursor, int pageCount) {
        RecipeSearchCursor searchNaturalRecipeList = searchFeignClient.searchByNaturalQuery(query, size, cursor, pageCount);

        List<Recipe> recipes = recipeRepository.findByIdInAndIsDeletedFalse(searchNaturalRecipeList.getRecipeIds());

        return RecipeSearchCursorResponse.builder()
                .recipes(recipes)
                .nextCursor(searchNaturalRecipeList.getNextCursor())
                .currentCount(searchNaturalRecipeList.getCurrentCount())
                .hasNext(searchNaturalRecipeList.isHasNext())
                .pageCount(searchNaturalRecipeList.getPageCount())
                .totalHits(searchNaturalRecipeList.getTotalHits())
                .totalPages(searchNaturalRecipeList.getTotalPages())
                .build();
    }

    public RecipeSearchCursorResponse searchByTags(List<String> tags, boolean matchAll, int size, String cursor, int pageCount) {
        RecipeSearchCursor searchTagsRecipeList = searchFeignClient.searchByTagsWithPagination(tags, matchAll, size, cursor, pageCount);

        List<Recipe> recipes = recipeRepository.findByIdInAndIsDeletedFalse(searchTagsRecipeList.getRecipeIds());

        return RecipeSearchCursorResponse.builder()
                .recipes(recipes)
                .nextCursor(searchTagsRecipeList.getNextCursor())
                .currentCount(searchTagsRecipeList.getCurrentCount())
                .hasNext(searchTagsRecipeList.isHasNext())
                .pageCount(searchTagsRecipeList.getPageCount())
                .totalHits(searchTagsRecipeList.getTotalHits())
                .totalPages(searchTagsRecipeList.getTotalPages())
                .build();
    }
}
