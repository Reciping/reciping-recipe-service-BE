package com.three.recipingrecipeservicebe.recipeDetailPage.service;

import com.three.recipingrecipeservicebe.bookmark.service.RecipeBookmarkService;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeDetailResponseDto;
import com.three.recipingrecipeservicebe.recipe.service.RecipeService;
import com.three.recipingrecipeservicebe.recipeDetailPage.dto.CommentResponseDto;
import com.three.recipingrecipeservicebe.recipeDetailPage.dto.LikeStatusResponseDto;
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
    private final RecipeBookmarkService recipeBookmarkService;
    private final CommentFeignClient commentFeignClient;
    private final LikeFeignClient likeFeignClient;

    public RecipeDetailAggregateDto getRecipeDetail(Long userId, Long recipeId) {
        // 1. 레시피 조회
        RecipeDetailResponseDto recipeDto = recipeService.getRecipeById(userId, recipeId);

        // 2. 댓글 조회
        List<CommentResponseDto> comments = commentFeignClient.getCommentsByRecipeId(recipeId);

        // 3. 좋아요 상태 조회
        LikeStatusResponseDto likeStatus = likeFeignClient.getRecipeLikeStatus(recipeId, userId);

        // 4. 북마크 여부 조회
        boolean isBookmarked = recipeBookmarkService.isBookmarked(userId, recipeId);

        // 5. 조합
        RecipeDetailResponseDto updatedRecipeDto = recipeDto.toBuilder()
                .likeCount(likeStatus.getLikeCount())
                .isLiked(likeStatus.isLiked())
                .isBookmarked(isBookmarked)
                .build();

        // 4. 응답 조합
        return new RecipeDetailAggregateDto(updatedRecipeDto, comments);
    }
}
