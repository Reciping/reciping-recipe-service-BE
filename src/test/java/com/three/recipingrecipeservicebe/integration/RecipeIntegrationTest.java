package com.three.recipingrecipeservicebe.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeRequestDto;
import com.three.recipingrecipeservicebe.recipe.entity.Recipe;
import com.three.recipingrecipeservicebe.recipe.mapper.RecipeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RecipeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RecipeRepository recipeRepository;

    private final Long testUserId = 1L;

    @Test
    @DisplayName("CREATE 이미지 없는 레시피 생성")
    void createRecipe() throws Exception {
        // given
        RecipeRequestDto dto = RecipeRequestDto.builder()
                .title("김치볶음밥")
                .content("1. 김치 썰기\n2. 밥 볶기")
                .build();

        // JSON DTO를 multipart file로 래핑
        MockMultipartFile requestDto = new MockMultipartFile(
                "requestDto",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(dto)
        );

        // when
        MvcResult result = mockMvc.perform(multipart("/api/v1/recipes")
                        .file(requestDto)
                        .header("X-USER-ID", testUserId))
                .andExpect(status().isOk())
                .andReturn();

        // then
        String responseBody = result.getResponse().getContentAsString();
        System.out.println("응답 받은 ID: " + responseBody);

        List<Recipe> all = recipeRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getTitle()).isEqualTo("김치볶음밥");
    }

    @Test
    @DisplayName("UPDATE 레시피 수정 (multipart/form-data)")
    void updateRecipe() throws Exception {
        // given
        Recipe recipe = Recipe.builder()
                .title("김치볶음밥")
                .content("1. 김치 썰기\n2. 밥 볶기")
                .userId(testUserId)
                .build();
        recipeRepository.save(recipe);

        RecipeRequestDto requestDto = RecipeRequestDto.builder()
                .title("참치김치볶음밥")
                .content("1. 참치와 김치 볶기\n2. 밥 넣기")
                .build();

        MockMultipartFile requestDtoPart = new MockMultipartFile(
                "requestDto",                // @RequestPart 이름
                "",                          // 파일 이름 (빈 값 허용)
                "application/json",
                objectMapper.writeValueAsBytes(requestDto)
        );

        // when
        MvcResult result = mockMvc.perform(
                        multipart("/api/v1/recipes/" + recipe.getId())
                                .file(requestDtoPart)
                                .with(request -> {
                                    request.setMethod("PUT"); // PUT 메서드 강제
                                    return request;
                                })
                                .header("X-USER-ID", testUserId)
                )
                .andExpect(status().isNoContent())
                .andReturn();

        System.out.println("응답 상태 코드: " + result.getResponse().getStatus());

        // then
        Recipe updated = recipeRepository.findById(recipe.getId()).orElseThrow();
        assertThat(updated.getTitle()).isEqualTo("참치김치볶음밥");
        assertThat(updated.getContent()).isEqualTo("1. 참치와 김치 볶기\n2. 밥 넣기");
        assertThat(updated.getUserId()).isEqualTo(testUserId);
    }

    @Test
    @DisplayName("DELETE 레시피 삭제")
    void deleteRecipe() throws Exception {
        // given
        Recipe recipe = Recipe.builder()
                .title("김치볶음밥")
                .content("1. 김치 썰기\n2. 밥 볶기")
                .userId(testUserId)
                .build();
        recipeRepository.save(recipe);

        // when
        MvcResult result = mockMvc.perform(
                        delete("/api/v1/recipes/" + recipe.getId())
                                .header("X-USER-ID", testUserId)
                )
                .andExpect(status().isNoContent()) // 204 상태 코드를 기대
                .andReturn();

        // then
        // 응답 상태 출력
        System.out.println("응답 상태 코드: " + result.getResponse().getStatus());

        // 응답 본문 출력 (응답이 없으면 빈 문자열이 출력됨)
        System.out.println("응답 본문: " + result.getResponse().getContentAsString());

        // 레시피가 삭제되었는지 확인
        List<Recipe> allRecipes = recipeRepository.findAll();
        assertThat(allRecipes).hasSize(0);
    }
}
