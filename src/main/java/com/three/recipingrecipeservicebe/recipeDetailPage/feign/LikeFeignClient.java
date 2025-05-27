package com.three.recipingrecipeservicebe.recipeDetailPage.feign;

import com.three.recipingrecipeservicebe.recipeDetailPage.dto.LikeStatusListRequestDto;
import com.three.recipingrecipeservicebe.recipeDetailPage.dto.RecipeLikeStatusListResponseDto;
import com.three.recipingrecipeservicebe.recipeDetailPage.dto.RecipeLikeStatusResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "reciping-like-service-BE", url = "http://localhost:8082")
public interface LikeFeignClient {

    @GetMapping("/api/v1/likes/recipe/{recipeId}/status")
    RecipeLikeStatusResponseDto getRecipeLikeStatus(
            @PathVariable("recipeId") Long recipeId,
            @RequestParam(value = "userId", required = false) Long userId
    );

    @PostMapping("/api/v1/likes/recipe/status-list")
    RecipeLikeStatusListResponseDto getLikeStatusForRecipes(
            @RequestBody LikeStatusListRequestDto requestDto
    );

}
