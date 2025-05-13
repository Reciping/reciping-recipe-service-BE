package com.three.recipingrecipeservicebe.recipeDetailPage.controller;

import com.three.recipingrecipeservicebe.recipe.dto.RecipeListResponseDto;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeSearchConditionRequestDto;
import com.three.recipingrecipeservicebe.recipeDetailPage.dto.RecipeDetailAggregateDto;
import com.three.recipingrecipeservicebe.recipeDetailPage.service.RecipeDetailFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recipes")
@RequiredArgsConstructor
public class RecipeDetailPageController {

    private final RecipeDetailFacade recipeDetailFacade;

    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeDetailAggregateDto> getRecipeDetail(
            @PathVariable Long recipeId,
            @RequestHeader("X-USER-ID") Long userId
    ) {
        RecipeDetailAggregateDto response = recipeDetailFacade.getRecipeDetail(userId, recipeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<RecipeListResponseDto> getMyRecipesWithLikes(
            @RequestHeader("X-USER-ID") Long userId,
            Pageable pageable
    ) {
        RecipeListResponseDto response = recipeDetailFacade.getMyRecipesWithLikes(userId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/bookmarked")
    public ResponseEntity<RecipeListResponseDto> getBookmarkedRecipesWithLikes(
            @RequestHeader("X-USER-ID") Long userId,
            Pageable pageable
    ) {
        RecipeListResponseDto response = recipeDetailFacade.getBookmarkedRecipesWithLikes(userId, pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/search/category")
    public ResponseEntity<RecipeListResponseDto> searchRecipes(
            @RequestBody RecipeSearchConditionRequestDto condition,
            Pageable pageable
    ) {
        RecipeListResponseDto results = recipeDetailFacade.searchRecipesWithLikes(condition, pageable);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/recommend")
    public ResponseEntity<RecipeListResponseDto> getRecommendedRecipes(
            @RequestHeader("X-USER-ID") Long userId,
            Pageable pageable
    ) {
        RecipeListResponseDto results = recipeDetailFacade.getRecommendListWithLikesResponseDto(userId, pageable);
        return ResponseEntity.ok(results);
    }
}
