package com.three.recipingrecipeservicebe.recipeDetailPage.service;

import com.three.recipingrecipeservicebe.recipe.dto.RecipeDetailResponseDto;
import com.three.recipingrecipeservicebe.recipe.entity.Recipe;
import com.three.recipingrecipeservicebe.recipe.entity.RecipeMapper;
import com.three.recipingrecipeservicebe.recipe.mapper.RecipeRepository;
import com.three.recipingrecipeservicebe.recipeDetailPage.dto.CommentResponseDto;
import com.three.recipingrecipeservicebe.recipeDetailPage.dto.RecipeDetailAggregateDto;
import com.three.recipingrecipeservicebe.recipeDetailPage.feign.CommentFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeDetailFacade {

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final CommentFeignClient commentFeignClient;

    public RecipeDetailAggregateDto getRecipeDetail(Long recipeId) {
        // 1. 레시피 조회 (RDB)
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));

        // 2. 댓글 조회 (댓글 서비스 호출)
        List<CommentResponseDto> comments = commentFeignClient.getCommentsByRecipeId(recipeId);

        // 3. 응답 조합
        RecipeDetailResponseDto recipeDto = recipeMapper.toDto(recipe);
        return new RecipeDetailAggregateDto(recipeDto, comments);
    }

}
