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
@RequestMapping("/api/v1/recipes") // Note: This controller shares /api/v1/recipes with RecipeController
@RequiredArgsConstructor
public class RecipeDetailPageController {

    private final RecipeDetailFacade recipeDetailFacade;
    private static final Logger logger = LoggerFactory.getLogger(RecipeDetailPageController.class);
    private static final Logger errorLogger = LoggerFactory.getLogger("ERROR_LOGGER");

    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeDetailAggregateDto> getRecipeDetail(
            @PathVariable Long recipeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails, // Can be null
            Pageable pageable,
            HttpServletRequest request
    ) {
        try {
            RecipeDetailAggregateDto response = recipeDetailFacade.getRecipeDetail(userDetails, recipeId, pageable);

            CustomLogger.track(
                    logger,
                    LogType.VIEW,
                    String.valueOf(recipeId), // targetId: recipeId being viewed
                    "{ \"pageable\": \"" + pageable.toString() + "\" }",
                    request
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            errorLogger.error("Error in RecipeDetailPageController.getRecipeDetail() for recipeId {}: {}", recipeId, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/default")
    public ResponseEntity<Page<RecipeSummaryResponseDto>> getDefaultRecipeListWithLikes(Pageable pageable, HttpServletRequest request) {
        try {
            Page<RecipeSummaryResponseDto> result = recipeDetailFacade.getDefaultRecipeListWithLikes(pageable);
            CustomLogger.track(
                    logger,
                    LogType.VIEW,
                    "default_recipe_list",
                    "Pageable: " + pageable.toString(),
                    request
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            errorLogger.error("Error in RecipeDetailPageController.getDefaultRecipeListWithLikes(): {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/myRecipes")
    public ResponseEntity<RecipeListResponseDto> getMyRecipesWithLikes(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Pageable pageable,
            HttpServletRequest request
    ) {
        try {
            RecipeListResponseDto response = recipeDetailFacade.getMyRecipesWithLikes(userDetails.getUserId(), pageable);
            CustomLogger.track(
                    logger,
                    LogType.VIEW,
                    String.valueOf(userDetails.getUserId()), // targetId: user viewing their recipes
                    "my_recipes_with_likes, Pageable: " + pageable.toString(),
                    request
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Long userId = (userDetails != null) ? userDetails.getUserId() : null;
            errorLogger.error("Error in RecipeDetailPageController.getMyRecipesWithLikes() for userId {}: {}", userId, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/bookmarked")
    public ResponseEntity<RecipeListResponseDto> getBookmarkedRecipesWithLikes(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Pageable pageable,
            HttpServletRequest request
    ) {
        try {
            RecipeListResponseDto response = recipeDetailFacade.getBookmarkedRecipesWithLikes(userDetails.getUserId(), pageable);
            CustomLogger.track(
                    logger,
                    LogType.VIEW,
                    String.valueOf(userDetails.getUserId()), // targetId: user viewing their bookmarked recipes
                    "bookmarked_recipes_with_likes, Pageable: " + pageable.toString(),
                    request
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Long userId = (userDetails != null) ? userDetails.getUserId() : null;
            errorLogger.error("Error in RecipeDetailPageController.getBookmarkedRecipesWithLikes() for userId {}: {}", userId, e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/search/category")
    public ResponseEntity<Page<RecipeSummaryResponseDto>> searchRecipes(
            @RequestBody RecipeSearchConditionRequestDto condition,
            Pageable pageable,
            HttpServletRequest request
    ) {
        try {
            Page<RecipeSummaryResponseDto> results = recipeDetailFacade.searchRecipesWithLikes(condition, pageable);
            CustomLogger.track(
                    logger,
                    LogType.SEARCH,
                    "category_search",
                    "{ \"condition\": " + JsonStringifier.toJsonString(condition) + ", \"pageable\": \"" + pageable.toString() + "\" }",
                    request
            );
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            errorLogger.error("Error in RecipeDetailPageController.searchRecipes(): {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/recommend")
    public ResponseEntity<RecipeListResponseDto> getRecommendedRecipes(
            @AuthenticationPrincipal UserDetailsImpl userDetails, // Can be null
            Pageable pageable,
            HttpServletRequest request
    ) {
        try {
            RecipeListResponseDto results = recipeDetailFacade.getRecommendListWithLikesResponseDto(pageable);
            String targetUser = (userDetails != null) ? String.valueOf(userDetails.getUserId()) : "anonymous";
            CustomLogger.track(
                    logger,
                    LogType.VIEW, // Or LogType.RECOMMENDATION_VIEW
                    targetUser,   // targetId: user for whom recommendations are (or general if anonymous)
                    "recommended_recipes, Pageable: " + pageable.toString(),
                    request
            );
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            errorLogger.error("Error in RecipeDetailPageController.getRecommendedRecipes(): {}", e.getMessage(), e);
            throw e;
        }
    }
}