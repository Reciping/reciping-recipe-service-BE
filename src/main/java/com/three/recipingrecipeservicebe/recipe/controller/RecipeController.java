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

    @GetMapping
    public ResponseEntity<Page<RecipeSummaryResponseDto>> getRecipeListByPage(Pageable pageable) {
        Page<RecipeSummaryResponseDto> response = recipeService.getRecipeListByPage(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<Response<RecipeCountResponseDto>> getMyRecipeCount(
            @RequestHeader("X-USER-ID") Long userId) {
        RecipeCountResponseDto response = recipeService.getMyRecipeCount(userId);
        return ResponseEntity.ok(Response.ok(response));
    }

    @PostMapping
    public ResponseEntity<Response<RecipeCreatedResponseDto>> createRecipe(
            @RequestPart("requestDto") @Valid RecipeRequestDto requestDto,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            HttpServletRequest request
    ) {
        RecipeCreatedResponseDto response = recipeService.createRecipe(requestDto, userDetails.getUserId(), file);

        CustomLogger.track(
                logger,
                LogType.RECIPE_CREATE,
                "/api/v1/recipes/" + response.getId(),
                "POST",
                String.valueOf(userDetails.getUserId()),
                null,
                String.valueOf(response.getId()),
                JsonStringifier.toJsonString(requestDto),
                request
        );

        CustomLogger.track(
                logger,
                LogType.TAGS,
                "/api/v1/recipes/" + response.getId(),
                "POST",
                String.valueOf(userDetails.getUserId()),
                null,
                String.valueOf(response.getId()),
                JsonStringifier.toJsonString(requestDto.getTags()),
                request
        );

        return ResponseEntity.ok(Response.ok(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRecipe(
            @PathVariable Long id,
            @RequestPart("requestDto") @Valid RecipeRequestDto requestDto,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            HttpServletRequest request
    ) {
        recipeService.updateRecipe(id, requestDto, userDetails.getUserId(), file);

        CustomLogger.track(
                logger,
                LogType.RECIPE_UPDATE,
                "/api/v1/recipes/" + id,
                "PUT",
                String.valueOf(userDetails.getUserId()),
                null,
                String.valueOf(id),
                JsonStringifier.toJsonString(requestDto),
                request
        );

        CustomLogger.track(
                logger,
                LogType.TAGS,
                "/api/v1/recipes/" + id,
                "PUT",
                String.valueOf(userDetails.getUserId()),
                null,
                String.valueOf(id),
                JsonStringifier.toJsonString(requestDto.getTags()),
                request
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteRecipe(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        recipeService.deleteRecipe(id, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category-options")
    public ResponseEntity<Map<String, List<Map<String, String>>>> getAllCategoryOptions() {
        return ResponseEntity.ok(recipeService.getAllCategoryOptions());
    }
}
