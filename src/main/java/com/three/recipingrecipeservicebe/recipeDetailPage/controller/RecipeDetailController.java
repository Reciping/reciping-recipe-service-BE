package com.three.recipingrecipeservicebe.recipeDetailPage.controller;

import com.three.recipingrecipeservicebe.recipeDetailPage.dto.RecipeDetailAggregateDto;
import com.three.recipingrecipeservicebe.recipeDetailPage.service.RecipeDetailFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recipe")
@RequiredArgsConstructor
public class RecipeDetailController {

    private final RecipeDetailFacade recipeDetailFacade;

    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeDetailAggregateDto> getRecipeDetail(
            @PathVariable Long recipeId,
            @RequestHeader("X-USER-ID") Long userId
    ) {
        RecipeDetailAggregateDto response = recipeDetailFacade.getRecipeDetail(userId, recipeId);
        return ResponseEntity.ok(response);
    }
}
