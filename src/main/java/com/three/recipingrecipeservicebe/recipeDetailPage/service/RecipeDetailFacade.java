package com.three.recipingrecipeservicebe.recipeDetailPage.service;

import com.three.recipingrecipeservicebe.recipe.dto.RecipeDetailResponseDto;
import com.three.recipingrecipeservicebe.recipe.service.RecipeService;
import com.three.recipingrecipeservicebe.recipeDetailPage.dto.CommentResponseDto;
import com.three.recipingrecipeservicebe.recipeDetailPage.dto.RecipeDetailAggregateDto;
import com.three.recipingrecipeservicebe.recipeDetailPage.feign.CommentFeignClient;
import com.three.recipingrecipeservicebe.recipeDetailPage.feign.LikeFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeDetailFacade {

    private final RecipeService recipeService;
    private final CommentFeignClient commentFeignClient;
    private final LikeFeignClient likeFeignClient;

    public RecipeDetailAggregateDto getRecipeDetail(Long userId, Long recipeId) {
        // 1. 레시피 조회 (RDB)
        RecipeDetailResponseDto recipeDto = recipeService.getRecipeById(userId, recipeId);

        // 2. 댓글 조회 (댓글 서비스 호출)
        List<CommentResponseDto> comments = commentFeignClient.getCommentsByRecipeId(recipeId);

        // 3. 좋아요 수 조회 (좋아요 서비스 호출)
        RecipeDetailResponseDto updatedRecipeDto = recipeDto.toBuilder()
                .likeCount(likeFeignClient.getLikeCount(recipeId))
                .build();

        // 4. 응답 조합
        return new RecipeDetailAggregateDto(updatedRecipeDto, comments);
    }
}
