package com.three.recipingrecipeservicebe.service;

import com.three.recipingrecipeservicebe.recipe.dto.RecipeDetailResponseDto;
import com.three.recipingrecipeservicebe.recipe.entity.Recipe;
import com.three.recipingrecipeservicebe.recipe.entity.RecipeMapper;
import com.three.recipingrecipeservicebe.recipe.mapper.RecipeRepository;
import com.three.recipingrecipeservicebe.recipeDetailPage.dto.CommentResponseDto;
import com.three.recipingrecipeservicebe.recipeDetailPage.dto.RecipeDetailAggregateDto;
import com.three.recipingrecipeservicebe.recipeDetailPage.feign.CommentFeignClient;
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
    private RecipeRepository recipeRepository;

    @Mock
    private CommentFeignClient commentFeignClient;

    @Mock
    private RecipeMapper recipeMapper;

    @InjectMocks
    private RecipeDetailFacade recipeDetailFacade;

    @Test
    @DisplayName("레시피 ID로 레시피와 댓글을 통합 조회한다")
    void getRecipeWithComments() {
        // given
        Long recipeId = 1L;

        Recipe recipe = Recipe.builder()
                .id(recipeId)
                .title("된장찌개")
                .content("재료 넣고 끓이기")
                .userId(10L)
                .build();

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

        given(recipeMapper.toDto(recipe)).willReturn(recipeDto);
        given(recipeRepository.findById(recipeId)).willReturn(Optional.of(recipe));
        given(commentFeignClient.getCommentsByRecipeId(recipeId)).willReturn(mockComments);

        // when
        RecipeDetailAggregateDto result = recipeDetailFacade.getRecipeDetail(recipeId);


        // then
        assertThat(result.getRecipe().getTitle()).isEqualTo("된장찌개");
        assertThat(result.getComments()).hasSize(2);
        assertThat(result.getComments().get(0).getContent()).isEqualTo("맛있겠어요");
    }
}