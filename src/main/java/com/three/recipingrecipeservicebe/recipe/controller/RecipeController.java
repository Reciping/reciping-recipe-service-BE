package com.three.recipingrecipeservicebe.recipe.controller;

import com.three.recipingrecipeservicebe.common.dto.Response;
import com.three.recipingrecipeservicebe.recipe.dto.*;
import com.three.recipingrecipeservicebe.recipe.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/v1/recipes")
@RestController
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping
    public ResponseEntity<List<RecipeSummaryResponseDto>> getRecipeListByPage(Pageable pageable) {
        List<RecipeSummaryResponseDto> response = recipeService.getRecipeListByPage(pageable);
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
//            @AuthenticationPrincipal(expression = "userId")
            @RequestHeader("X-USER-ID") Long userId
    ) {
        RecipeCreatedResponseDto response = recipeService.createRecipe(requestDto, userId, file);
        return ResponseEntity.ok(Response.ok(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRecipe(
            @PathVariable Long id,
            @RequestPart("requestDto") @Valid RecipeRequestDto requestDto,
            @RequestPart(value = "file", required = false) MultipartFile file,
            //            @AuthenticationPrincipal(expression = "id")
            @RequestHeader("X-USER-ID") Long userId
    ) {
        recipeService.updateRecipe(id, requestDto, userId, file);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteRecipe(
            @PathVariable Long id,
//            @AuthenticationPrincipal(expression = "id")
            @RequestHeader("X-USER-ID") Long userId
    ) {
        recipeService.deleteRecipe(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category-options")
    public ResponseEntity<Map<String, List<Map<String, String>>>> getAllCategoryOptions() {
        return ResponseEntity.ok(recipeService.getAllCategoryOptions());
    }
}
