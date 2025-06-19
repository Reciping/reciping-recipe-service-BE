package com.three.recipingrecipeservicebe.recipe.controller;

import com.three.recipingrecipeservicebe.common.dto.Response;
import com.three.recipingrecipeservicebe.global.logger.CustomLogger;
import com.three.recipingrecipeservicebe.global.logger.LogType;
import com.three.recipingrecipeservicebe.global.security.UserDetailsImpl;
import com.three.recipingrecipeservicebe.global.util.JsonStringifier;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeCountResponseDto;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeCreatedResponseDto;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeRequestDto;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeSummaryResponseDto;
import com.three.recipingrecipeservicebe.recipe.service.RecipeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/v1/recipes")
@RestController
public class RecipeController {

    private final RecipeService recipeService;
    private static final Logger logger = LoggerFactory.getLogger(RecipeController.class);
    private static final Logger errorLogger = LoggerFactory.getLogger("ERROR_LOGGER");

    @GetMapping
    public ResponseEntity<Page<RecipeSummaryResponseDto>> getRecipeListByPage(Pageable pageable, HttpServletRequest request) {
        try {
            Page<RecipeSummaryResponseDto> response = recipeService.getRecipeListByPage(pageable);

            CustomLogger.track(
                    logger,
                    LogType.VIEW,
                    "recipe_list",
                    "Pageable: " + pageable.toString(),
                    request
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            errorLogger.error("Error in RecipeController.getRecipeListByPage(): {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Response<RecipeCountResponseDto>> getMyRecipeCount(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request) {
        try {
            Long userId = userDetails.getUserId();
            RecipeCountResponseDto response = recipeService.getMyRecipeCount(userId);

            CustomLogger.track(
                    logger,
                    LogType.VIEW,
                    String.valueOf(userId), // targetId: user whose recipe count is fetched
                    "my_recipe_count",
                    request
            );

            return ResponseEntity.ok(Response.ok(response));
        } catch (Exception e) {
            Long userId = (userDetails != null) ? userDetails.getUserId() : null;
            errorLogger.error("Error in RecipeController.getMyRecipeCount() for userId {}: {}", userId, e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<Response<RecipeCreatedResponseDto>> createRecipe(
            @RequestPart("requestDto") @Valid RecipeRequestDto requestDto,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            HttpServletRequest request
    ) {
        RecipeCreatedResponseDto response = null;
        try {
            response = recipeService.createRecipe(requestDto, userDetails.getUserId(), file);

            CustomLogger.track(
                    logger,
                    LogType.RECIPE_CREATE,
                    String.valueOf(response.getId()), // targetId: created recipe ID
                    JsonStringifier.toJsonString(requestDto), // payload: request DTO
                    request
            );

            if (requestDto.getTags() != null && !requestDto.getTags().isEmpty()) {
                CustomLogger.track(
                        logger,
                        LogType.TAGS,
                        String.valueOf(response.getId()), // targetId: created recipe ID
                        JsonStringifier.toJsonString(requestDto.getTags()), // payload: tags
                        request
                );
            }

            return ResponseEntity.ok(Response.ok(response));
        } catch (Exception e) {
            Long recipeId = (response != null) ? response.getId() : null;
            errorLogger.error("Error in RecipeController.createRecipe() for recipeId {}: {}", recipeId, e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRecipe(
            @PathVariable Long id,
            @RequestPart("requestDto") @Valid RecipeRequestDto requestDto,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            HttpServletRequest request
    ) {
        try {
            recipeService.updateRecipe(id, requestDto, userDetails.getUserId(), file);

            CustomLogger.track(
                    logger,
                    LogType.RECIPE_UPDATE,
                    String.valueOf(id), // targetId: updated recipe ID
                    JsonStringifier.toJsonString(requestDto), // payload: request DTO
                    request
            );

            if (requestDto.getTags() != null && !requestDto.getTags().isEmpty()) {
                CustomLogger.track(
                        logger,
                        LogType.TAGS,
                        String.valueOf(id), // targetId: updated recipe ID
                        JsonStringifier.toJsonString(requestDto.getTags()), // payload: tags
                        request
                );
            }

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            errorLogger.error("Error in RecipeController.updateRecipe() for recipeId {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteRecipe(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            HttpServletRequest request
    ) {
        try {
            recipeService.deleteRecipe(id, userDetails.getUserId());

            CustomLogger.track(
                    logger,
                    LogType.RECIPE_DELETE, // Assuming LogType.RECIPE_DELETE exists
                    String.valueOf(id),   // targetId: deleted recipe ID
                    "User: " + userDetails.getUserId(), // payload: user performing delete
                    request
            );

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            errorLogger.error("Error in RecipeController.deleteRecipe() for recipeId {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/category-options")
    public ResponseEntity<Map<String, List<Map<String, String>>>> getAllCategoryOptions(HttpServletRequest request) {
        try {
            Map<String, List<Map<String, String>>> categoryOptions = recipeService.getAllCategoryOptions();
            CustomLogger.track(
                    logger,
                    LogType.VIEW,
                    "category_options",
                    "Fetched all category options",
                    request
            );
            return ResponseEntity.ok(categoryOptions);
        } catch (Exception e) {
            errorLogger.error("Error in RecipeController.getAllCategoryOptions(): {}", e.getMessage(), e);
            throw e;
        }
    }
}