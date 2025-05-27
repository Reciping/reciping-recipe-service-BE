package com.three.recipingrecipeservicebe.recipeDetailPage.controller;

import com.three.recipingrecipeservicebe.global.logger.CustomLogger;
import com.three.recipingrecipeservicebe.global.logger.LogType;
import com.three.recipingrecipeservicebe.global.security.UserDetailsImpl;
import com.three.recipingrecipeservicebe.global.util.JsonStringifier;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeListResponseDto;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeSearchConditionRequestDto;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeSummaryResponseDto;
import com.three.recipingrecipeservicebe.recipeDetailPage.dto.RecipeDetailAggregateDto;
import com.three.recipingrecipeservicebe.recipeDetailPage.service.RecipeDetailFacade;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recipes")
@RequiredArgsConstructor
public class RecipeDetailPageController {

    private final RecipeDetailFacade recipeDetailFacade;
    private static final Logger logger = LoggerFactory.getLogger(RecipeDetailPageController.class);

    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeDetailAggregateDto> getRecipeDetail(
            @PathVariable Long recipeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Pageable pageable,
            HttpServletRequest request
    ) {
        RecipeDetailAggregateDto response = recipeDetailFacade.getRecipeDetail(userDetails, recipeId, pageable);

        CustomLogger.track(
                logger,
                LogType.VIEW,
                "/api/v1/recipes/" + recipeId,
                "GET",
                (userDetails != null) ? String.valueOf(userDetails.getUserId()) : null,
                null,
                null,
                JsonStringifier.toJsonString(response),
                request
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/default")
    public ResponseEntity<Page<RecipeSummaryResponseDto>> getDefaultRecipeListWithLikes(Pageable pageable) {
        Page<RecipeSummaryResponseDto> result = recipeDetailFacade.getDefaultRecipeListWithLikes(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/myRecipes")
    public ResponseEntity<RecipeListResponseDto> getMyRecipesWithLikes(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Pageable pageable
    ) {
        RecipeListResponseDto response = recipeDetailFacade.getMyRecipesWithLikes(userDetails.getUserId(), pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/bookmarked")
    public ResponseEntity<RecipeListResponseDto> getBookmarkedRecipesWithLikes(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Pageable pageable
    ) {
        RecipeListResponseDto response = recipeDetailFacade.getBookmarkedRecipesWithLikes(userDetails.getUserId(), pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/search/category")
    public ResponseEntity<Page<RecipeSummaryResponseDto>> searchRecipes(
            @RequestBody RecipeSearchConditionRequestDto condition,
            Pageable pageable
    ) {
        Page<RecipeSummaryResponseDto> results = recipeDetailFacade.searchRecipesWithLikes(condition, pageable);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/recommend")
    public ResponseEntity<RecipeListResponseDto> getRecommendedRecipes(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Pageable pageable
    ) {
        RecipeListResponseDto results = recipeDetailFacade.getRecommendListWithLikesResponseDto(pageable);
        return ResponseEntity.ok(results);
    }
}
