package com.three.recipingrecipeservicebe.recipe.controller;

import com.three.recipingrecipeservicebe.common.dto.Response;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeCountResponseDto;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeCreatedResponseDto;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeListResponseDto;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeRequestDto;
import com.three.recipingrecipeservicebe.recipe.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/recipes")
@RestController
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping
    public ResponseEntity<List<RecipeListResponseDto>> getRecipeListByPage(Pageable pageable) {
        List<RecipeListResponseDto> response = recipeService.getRecipeListByPage(pageable);
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
}
