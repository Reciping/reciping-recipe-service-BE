package com.three.recipingrecipeservicebe.recipeDetailPage.service;

import com.three.recipingrecipeservicebe.bookmark.service.RecipeBookmarkService;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeListResponseDto;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeSearchConditionRequestDto;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeSummaryResponseDto;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeDetailResponseDto;
import com.three.recipingrecipeservicebe.recipe.service.RecipeService;
import com.three.recipingrecipeservicebe.recipeDetailPage.dto.*;
import com.three.recipingrecipeservicebe.recipeDetailPage.feign.CommentFeignClient;
import com.three.recipingrecipeservicebe.recipeDetailPage.feign.LikeFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

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
        RecipeLikeStatusResponseDto likeStatus = likeFeignClient.getRecipeLikeStatus(recipeId, userId);

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

    public RecipeListResponseDto getMyRecipesWithLikes(Long userId, Pageable pageable) {
        Page<RecipeSummaryResponseDto> page = recipeService.getMyRecipes(userId, pageable);

        return getRecipeListWithLikesResponseDto(userId, page);
    }

    public RecipeListResponseDto getBookmarkedRecipesWithLikes(Long userId, Pageable pageable) {
        Page<RecipeSummaryResponseDto> page = recipeService.getBookmarkedRecipeList(userId, pageable);

        return getRecipeListWithLikesResponseDto(userId, page);
    }

    public RecipeListResponseDto searchRecipesWithLikes(RecipeSearchConditionRequestDto condition, Pageable pageable) {
        Page<RecipeSummaryResponseDto> page = recipeService.searchRecipes(condition, pageable);

        List<RecipeSummaryResponseDto> updatedList = getRecipeListWithSummaryResponseDto(page.getContent());

        return RecipeListResponseDto.builder()
                .recipes(updatedList)
                .page(page.getNumber())
                .totalPages(page.getTotalPages())
                .build();
    }

    private RecipeListResponseDto getRecipeListWithLikesResponseDto(Long userId, Page<RecipeSummaryResponseDto> page) {
        List<Long> recipeIds = page.getContent().stream()
                .map(RecipeSummaryResponseDto::getId)
                .toList();

        // 3. 좋아요 상태 조회 (비페이징)
        LikeStatusListRequestDto requestDto = LikeStatusListRequestDto.builder()
                .recipeIdList(recipeIds)
                .userId(userId)
                .build();

        RecipeLikeStatusListResponseDto response = likeFeignClient.getLikeStatusForRecipes(requestDto);
        List<RecipeLikeStatusResponseDto> likeList = response.getData();

        List<RecipeSummaryResponseDto> updatedList = IntStream.range(0, page.getContent().size())
                .mapToObj(i -> {
                    RecipeSummaryResponseDto dto = page.getContent().get(i);
                    RecipeLikeStatusResponseDto like = likeList.get(i);

                    return dto.toBuilder()
                            .likeCount(like.getLikeCount().intValue())
                            .isLiked(like.isLiked())
                            .build();
                })
                .toList();

        // 5. 응답 DTO 조립 후 반환
        return RecipeListResponseDto.builder()
                .recipes(updatedList)
                .page(page.getNumber())
                .totalPages(page.getTotalPages())
                .build();
    }

    private List<RecipeSummaryResponseDto> getRecipeListWithSummaryResponseDto(List<RecipeSummaryResponseDto> recipes) {
        if (recipes.isEmpty()) return List.of();

        List<Long> recipeIds = recipes.stream()
                .map(RecipeSummaryResponseDto::getId)
                .toList();

        // 유저 ID 없이 좋아요 개수만 요청
        LikeStatusListRequestDto requestDto = LikeStatusListRequestDto.builder()
                .recipeIdList(recipeIds)
                .build();

        RecipeLikeStatusListResponseDto response = likeFeignClient.getLikeStatusForRecipes(requestDto);
        List<RecipeLikeStatusResponseDto> likeList = response.getData();

        return IntStream.range(0, recipes.size())
                .mapToObj(i -> {
                    RecipeSummaryResponseDto dto = recipes.get(i);
                    RecipeLikeStatusResponseDto like = likeList.get(i);

                    return dto.toBuilder()
                            .likeCount(like.getLikeCount().intValue())
                            .build(); // isLiked는 무시
                })
                .toList();
    }
}
