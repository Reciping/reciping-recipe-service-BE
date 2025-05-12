package com.three.recipingrecipeservicebe.integration;

import com.three.recipingrecipeservicebe.bookmark.entity.RecipeBookmarkDocument;
import com.three.recipingrecipeservicebe.bookmark.repository.RecipeBookmarkRepository;
import com.three.recipingrecipeservicebe.recipe.entity.Recipe;
import com.three.recipingrecipeservicebe.recipe.repository.RecipeRepository;
import com.three.recipingrecipeservicebe.recipeDetailPage.dto.CommentResponseDto;
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
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@TestPropertySource(properties = {
//        "spring.data.mongodb.uri=mongodb://localhost:27017/recipe_bookmark"
//})
@SpringBootTest
@AutoConfigureMockMvc
public class RecipeDetailIntegrationTest {

    @Autowired private MockMvc mockMvc;

    // TODO [강산하] [2025-05-11]: 현재 테스트는 Deprecated 된 @MockBean 사용중이므로 추후 직접 연결 시 제거

    @MockBean private RecipeBookmarkRepository recipeBookmarkRepository;
    @MockBean private RecipeRepository recipeRepository;

    @MockBean private LikeFeignClient likeFeignClient;
    @MockBean private CommentFeignClient commentFeignClient;

    private final Long userId = 1L;
    private final Long recipeId = 1L;

    @BeforeEach
    void setUp() {
        Recipe recipe = Recipe.builder()
                .id(recipeId)
                .userId(userId)
                .title("테스트 레시피")
                .content("테스트 내용")
                .cookingTime(30)
                .difficulty("중간")
                .build();



        RecipeBookmarkDocument bookmarkDoc = RecipeBookmarkDocument.builder()
                .userId(userId)
                .recipeId(recipeId)
                .createdAt(LocalDateTime.now())
                .build();

        // Stub: 저장 메서드
        given(recipeRepository.save(any())).willReturn(recipe);
        given(recipeRepository.findById(recipeId)).willReturn(Optional.of(recipe));
        given(recipeBookmarkRepository.save(any())).willReturn(bookmarkDoc);

        // Stub: 북마크 상태
        given(recipeBookmarkRepository.findByUserIdAndRecipeId(userId, recipeId))
                .willReturn(Optional.of(bookmarkDoc));

        // Stub: 좋아요 API 응답
        given(likeFeignClient.getRecipeLikeStatus(recipeId, userId))
                .willReturn(RecipeLikeStatusResponseDto.builder()
                        .recipeId(recipeId)
                        .likeCount(7L)
                        .isLiked(true)
                        .build());

        // Stub: 댓글 API 응답
        given(commentFeignClient.getCommentsByRecipeId(recipeId))
                .willReturn(List.of(
                        CommentResponseDto.builder()
                                .userId(userId)
                                .recipeId(recipeId)
                                .content("댓글입니다")
                                .createdAt(LocalDateTime.now())
                                .build()
                ));
    }

    @Test
    @DisplayName("READ 레시피 상세 정보 + 북마크 + 좋아요 + 댓글 조회")
    void getRecipeDetail_allIntegrated() throws Exception {
        mockMvc.perform(get("/api/v1/recipes/{recipeId}", recipeId)
                        .header("X-USER-ID", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipe.id").value(recipeId))
                .andExpect(jsonPath("$.recipe.bookmarked").value(true))
                .andExpect(jsonPath("$.recipe.liked").value(true))
                .andExpect(jsonPath("$.recipe.likeCount").value(7))
                .andExpect(jsonPath("$.comments[0].content").value("댓글입니다"));
    }
}
