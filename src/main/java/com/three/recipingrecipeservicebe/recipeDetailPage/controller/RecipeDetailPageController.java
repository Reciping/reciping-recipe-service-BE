package com.three.recipingrecipeservicebe.recipeDetailPage.controller;

import com.three.recipingrecipeservicebe.recipe.dto.MyRecipeListResponseDto;
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
    public ResponseEntity<MyRecipeListResponseDto> getMyRecipesWithLikes(
            @RequestHeader("X-USER-ID") Long userId,
            Pageable pageable
    ) {
        MyRecipeListResponseDto response = recipeDetailFacade.getMyRecipesWithLikes(userId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/bookmarked")
    public ResponseEntity<MyRecipeListResponseDto> getBookmarkedRecipesWithLikes(
            @RequestHeader("X-USER-ID") Long userId,
            Pageable pageable
    ) {
        MyRecipeListResponseDto response = recipeDetailFacade.getBookmarkedRecipesWithLikes(userId, pageable);
        return ResponseEntity.ok(response);
    }
}
