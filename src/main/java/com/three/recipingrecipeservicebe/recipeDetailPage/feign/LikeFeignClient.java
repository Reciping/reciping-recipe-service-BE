package com.three.recipingrecipeservicebe.recipeDetailPage.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "reciping-like-service-BE", url = "http://localhost:8082")
public interface LikeFeignClient {
    @GetMapping("/api/v1/likes/recipe/{recipeId}/count")
    Long getLikeCount(@PathVariable("recipeId") Long recipeId);
}
