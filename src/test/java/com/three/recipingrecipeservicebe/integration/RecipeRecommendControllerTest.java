package com.three.recipingrecipeservicebe.integration;

import com.three.recipingrecipeservicebe.recipe.entity.Recipe;
import com.three.recipingrecipeservicebe.recipe.repository.RecipeRepository;
import com.three.recipingrecipeservicebe.recipeDetailPage.dto.RecipeLikeStatusListResponseDto;
import com.three.recipingrecipeservicebe.recipeDetailPage.dto.RecipeLikeStatusResponseDto;
import com.three.recipingrecipeservicebe.recipeDetailPage.feign.LikeFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RecipeRecommendControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LikeFeignClient likeFeignClient;

    @Autowired
    private RecipeRepository recipeRepository;

    @BeforeEach
    void setUp() {
        recipeRepository.deleteAll(); // 테스트 전 초기화

        List<Recipe> recipes = IntStream.rangeClosed(1, 10)
                .mapToObj(i -> Recipe.builder()
                        .title("추천 레시피 " + i)
                        .userId(1L)
                        .isDeleted(false)
                        .build())
                .collect(Collectors.toList());

        recipeRepository.saveAll(recipes);
    }

    @Test
    @DisplayName("레시피 추천 목록 조회 - 좋아요 10개 mock")
    void getRecommendedRecipes_withMockLikes() throws Exception {
        // given
        Long userId = 1L;

        List<RecipeLikeStatusResponseDto> likeStatuses = IntStream.rangeClosed(1, 10)
                .mapToObj(i -> RecipeLikeStatusResponseDto.builder()
                        .recipeId((long) i)
                        .isLiked(true)
                        .likeCount(10L)
                        .build())
                .collect(Collectors.toList());

        RecipeLikeStatusListResponseDto mockResponse = RecipeLikeStatusListResponseDto.builder()
                .data(likeStatuses)
                .build();

        when(likeFeignClient.getLikeStatusForRecipes(any())).thenReturn(mockResponse);

        // when + then
        mockMvc.perform(get("/api/v1/recipes/recommend")
                        .header("X-USER-ID", String.valueOf(userId))
                        .param("page", "0")
                        .param("size", "5"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipes").isArray())
                .andExpect(jsonPath("$.recipes.length()").value(5));
    }
}
