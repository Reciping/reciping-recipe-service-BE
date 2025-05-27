package com.three.recipingrecipeservicebe.recipeDetailPage.feign;

import com.three.recipingrecipeservicebe.recipeDetailPage.dto.CommentResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "reciping-comment-service-BE", url = "http://localhost:8086")
public interface CommentFeignClient {
    @GetMapping("/api/v1/comments/recipe/{recipeId}")
    Page<CommentResponseDto> getCommentsByRecipeId(@PathVariable("recipeId") Long recipeId, Pageable pageable);
}
