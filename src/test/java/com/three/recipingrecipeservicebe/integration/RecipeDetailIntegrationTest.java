package com.three.recipingrecipeservicebe.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.three.recipingrecipeservicebe.bookmark.entity.RecipeBookmarkDocument;
import com.three.recipingrecipeservicebe.bookmark.repository.RecipeBookmarkRepository;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeSearchConditionRequestDto;
import com.three.recipingrecipeservicebe.recipe.entity.*;
import com.three.recipingrecipeservicebe.recipe.repository.RecipeQueryRepository;
import com.three.recipingrecipeservicebe.recipe.repository.RecipeRepository;
import com.three.recipingrecipeservicebe.recipeDetailPage.dto.CommentResponseDto;
import com.three.recipingrecipeservicebe.recipeDetailPage.dto.RecipeLikeStatusListResponseDto;
import com.three.recipingrecipeservicebe.recipeDetailPage.dto.RecipeLikeStatusResponseDto;
import com.three.recipingrecipeservicebe.recipeDetailPage.feign.CommentFeignClient;
import com.three.recipingrecipeservicebe.recipeDetailPage.feign.LikeFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.data.mongodb.uri=mongodb://localhost:27017/recipe_bookmark"
})
@Transactional
public class RecipeDetailIntegrationTest {

    @Autowired private MockMvc mockMvc;

    // TODO [강산하] [2025-05-11]: 현재 테스트는 Deprecated 된 @MockBean 사용중이므로 추후 직접 연결 시 제거

    @Autowired
    private RecipeBookmarkRepository recipeBookmarkRepository;
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeQueryRepository recipeQueryRepositoryImpl;

    @MockBean private LikeFeignClient likeFeignClient;
    @MockBean private CommentFeignClient commentFeignClient;

    @Autowired
    private ObjectMapper objectMapper;

    private final Long userId = 1000L;
    private Long recipeId1;
    private Long recipeId2;

    @BeforeEach
    void setUp() {
        // 1. 테스트용 레시피 데이터 저장
        Recipe saved1 = recipeRepository.save(
                Recipe.builder()
                        .userId(userId)
                        .title("김치볶음밥")
                        .content("매콤한 김치볶음밥 레시피")
                        .cookingTime(15)
                        .difficulty("easy")
                        .dishType(DishType.BANCHAN)
                        .situationType(SituationType.DAILY)
                        .methodType(MethodType.STIR_FRY)
                        .ingredientType(IngredientType.RICE)
                        .build()
        );

        Recipe saved2 = recipeRepository.save(
                Recipe.builder()
                        .userId(userId)
                        .title("된장찌개")
                        .content("구수한 된장찌개 레시피")
                        .cookingTime(20)
                        .difficulty("medium")
                        .dishType(DishType.STEW)
                        .situationType(SituationType.LATE_NIGHT)
                        .methodType(MethodType.BOIL)
                        .ingredientType(IngredientType.VEGETABLE)
                        .build()
        );

        recipeId1 = saved1.getId();
        recipeId2 = saved2.getId();

        // 좋아요 Mock 데이터
        List<RecipeLikeStatusResponseDto> likes = List.of(
                RecipeLikeStatusResponseDto.builder()
                        .recipeId(recipeId1)
                        .likeCount(10L)
                        .isLiked(true)
                        .build(),
                RecipeLikeStatusResponseDto.builder()
                        .recipeId(recipeId2)
                        .likeCount(5L)
                        .isLiked(false)
                        .build()
        );

        // 응답 래퍼 DTO
        RecipeLikeStatusListResponseDto likeResponse = RecipeLikeStatusListResponseDto.builder()
                .data(likes)
                .build();

        given(likeFeignClient.getLikeStatusForRecipes(any()))
                .willReturn(likeResponse);
    }

    @Test
    @DisplayName("READ 검색 - DishType이 BANCHAN인 레시피만 반환")
    void search_filterByDishType() throws Exception {
        RecipeSearchConditionRequestDto condition = RecipeSearchConditionRequestDto.builder()
                .dish(DishType.BANCHAN)
                .build();

        mockMvc.perform(post("/api/v1/recipes/categorySearch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(condition)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipes.length()").value(1))
                .andExpect(jsonPath("$.recipes[0].title").value("김치볶음밥"));
    }

}
