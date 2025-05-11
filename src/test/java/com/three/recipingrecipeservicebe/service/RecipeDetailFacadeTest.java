package com.three.recipingrecipeservicebe.service;

import com.three.recipingrecipeservicebe.bookmark.service.RecipeBookmarkService;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeDetailResponseDto;
import com.three.recipingrecipeservicebe.recipe.service.RecipeService;
import com.three.recipingrecipeservicebe.recipeDetailPage.dto.CommentResponseDto;
import com.three.recipingrecipeservicebe.recipeDetailPage.dto.LikeStatusResponseDto;
import com.three.recipingrecipeservicebe.recipeDetailPage.dto.RecipeDetailAggregateDto;
import com.three.recipingrecipeservicebe.recipeDetailPage.feign.CommentFeignClient;
import com.three.recipingrecipeservicebe.recipeDetailPage.feign.LikeFeignClient;
import com.three.recipingrecipeservicebe.recipeDetailPage.service.RecipeDetailFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RecipeDetailFacadeTest {

    @Mock
    private RecipeService recipeService;

    @Mock
    private CommentFeignClient commentFeignClient;

    @Mock
    private RecipeBookmarkService recipeBookmarkService;

    @Mock
    private LikeFeignClient likeFeignClient;


    @InjectMocks
    private RecipeDetailFacade recipeDetailFacade;

    @Test
    @DisplayName("READ 레시피 ID로 레시피와 댓글을 통합 조회한다")
    void getRecipeWithComments() {
        // given
        Long recipeId = 1L;
        Long userId = 2L;

        RecipeDetailResponseDto recipeDto = RecipeDetailResponseDto.builder()
                .id(recipeId)
                .title("된장찌개")
                .content("재료 넣고 끓이기")
                .userId(10L)
                .build();

        List<CommentResponseDto> mockComments = List.of(
                new CommentResponseDto("1", recipeId, 11L, "맛있겠어요", LocalDateTime.now(), LocalDateTime.now()),
                new CommentResponseDto("2", recipeId, 12L, "오늘 해먹었어요", LocalDateTime.now(), LocalDateTime.now())
        );

        given(recipeService.getRecipeById(userId, recipeId)).willReturn(recipeDto);
        given(commentFeignClient.getCommentsByRecipeId(recipeId)).willReturn(mockComments);
        given(likeFeignClient.getRecipeLikeStatus(recipeId, userId))
                .willReturn(LikeStatusResponseDto.builder()
                        .recipeId(recipeId)
                        .likeCount(5L)
                        .isLiked(true)
                        .build());

        // when
        RecipeDetailAggregateDto result = recipeDetailFacade.getRecipeDetail(userId, recipeId);

        // then
        assertThat(result.getRecipe().getTitle()).isEqualTo("된장찌개");
        assertThat(result.getRecipe().getLikeCount()).isEqualTo(5L); // ← 추가 확인
        assertThat(result.getComments()).hasSize(2);
        assertThat(result.getComments().get(0).getContent()).isEqualTo("맛있겠어요");
    }
}