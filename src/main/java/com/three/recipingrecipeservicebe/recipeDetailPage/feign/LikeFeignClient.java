package com.three.recipingrecipeservicebe.recipeDetailPage.feign;

import com.three.recipingrecipeservicebe.recipeDetailPage.dto.LikeStatusResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "reciping-like-service-BE", url = "http://localhost:8082")
public interface LikeFeignClient {

    @GetMapping("/api/v1/likes/recipe/{recipeId}/status")
    LikeStatusResponseDto getRecipeLikeStatus(
            @PathVariable("recipeId") Long recipeId,
            @RequestParam("userId") Long userId
    );
}
