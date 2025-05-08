package com.three.recipingrecipeservicebe.recipeDetailPage.feign;

import com.three.recipingrecipeservicebe.recipeDetailPage.dto.CommentResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "reciping-comment-service-BE", url = "http://localhost:8081")
public interface CommentFeignClient {
    @GetMapping("/api/v1/comments/recipe/{recipeId}")
    List<CommentResponseDto> getCommentsByRecipeId(@PathVariable("recipeId") Long recipeId);
}
