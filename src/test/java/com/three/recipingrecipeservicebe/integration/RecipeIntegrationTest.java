package com.three.recipingrecipeservicebe.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.three.recipingrecipeservicebe.hashtag.entity.RecipeTagDocument;
import com.three.recipingrecipeservicebe.hashtag.repository.RecipeTagRepository;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeRequestDto;
import com.three.recipingrecipeservicebe.recipe.entity.Recipe;
import com.three.recipingrecipeservicebe.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Autowired
    private RecipeTagRepository recipeTagRepository;

    private final Long testUserId = 1L;

    @BeforeEach
    void setUp() {
        recipeRepository.deleteAll();
        recipeTagRepository.deleteAll();
    }

    @Test
    @DisplayName("CREATE 이미지 없는 레시피 생성")
    void createRecipe() throws Exception {
        // given
        RecipeRequestDto dto = RecipeRequestDto.builder()
                .userId(testUserId)
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
                .userId(testUserId)
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

    @Test
    @DisplayName("CREATE 태그가 포함된 레시피 생성")
    void createRecipe_withTags() throws Exception {
        // given
        RecipeRequestDto dto = RecipeRequestDto.builder()
                .userId(testUserId)
                .title("된장찌개")
                .content("된장에 채소 넣기")
                .tags(List.of("한식", "국물")) // ✅ 태그 포함
                .build();

        MockMultipartFile requestDto = new MockMultipartFile(
                "requestDto",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(dto)
        );

        // when
        MvcResult result = mockMvc.perform(
                        multipart("/api/v1/recipes")
                                .file(requestDto)
                                .header("X-USER-ID", testUserId)
                )
                .andExpect(status().isOk())
                .andReturn();

        // then
        String responseBody = result.getResponse().getContentAsString();
        System.out.println("응답 받은 ID: " + responseBody);

        // RDB 저장 확인
        List<Recipe> allRecipes = recipeRepository.findAll();
        assertThat(allRecipes).hasSize(1);
        Recipe savedRecipe = allRecipes.get(0);
        assertThat(savedRecipe.getTitle()).isEqualTo("된장찌개");

        // MongoDB 저장 확인
        RecipeTagDocument tagDoc = recipeTagRepository.findByRecipeId(savedRecipe.getId())
                .orElseThrow(() -> new IllegalStateException("태그 도큐먼트가 저장되지 않았습니다."));
        assertThat(tagDoc.getTags()).containsExactlyInAnyOrder("한식", "국물");
    }

    @Test
    @DisplayName("UPDATE 레시피 태그를 덮어쓰면 기존 태그가 새로운 태그로 바뀐다 (upsert)")
    void updateRecipe_overwriteTags() throws Exception {
        // given: 먼저 레시피 저장
        RecipeRequestDto dto1 = RecipeRequestDto.builder()
                .userId(testUserId)
                .title("된장찌개")
                .content("된장에 채소 넣기")
                .tags(List.of("한식", "국물")) // 초기 태그
                .build();

        MockMultipartFile requestDto1 = new MockMultipartFile(
                "requestDto", "", "application/json", objectMapper.writeValueAsBytes(dto1)
        );

        MvcResult result1 = mockMvc.perform(
                        multipart("/api/v1/recipes")
                                .file(requestDto1)
                                .header("X-USER-ID", testUserId))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result1.getResponse().getContentAsString();

        Long recipeId = objectMapper.readTree(responseBody).get("data").get("id").asLong();

        // then: 초기 태그 저장 확인
        RecipeTagDocument first = recipeTagRepository.findById(recipeId)
                .orElseThrow();
        assertThat(first.getTags()).containsExactlyInAnyOrder("한식", "국물");

        // when: 다른 태그로 덮어쓰기
        RecipeRequestDto dto2 = RecipeRequestDto.builder()
                .userId(testUserId)
                .title("된장찌개 수정")
                .content("변경된 설명")
                .tags(List.of("찌개", "매운맛")) // 새 태그로 덮어쓰기
                .build();

        MockMultipartFile requestDto2 = new MockMultipartFile(
                "requestDto", "", "application/json", objectMapper.writeValueAsBytes(dto2)
        );

        mockMvc.perform(
                        multipart("/api/v1/recipes/" + recipeId)
                                .file(requestDto2)
                                .with(req -> {
                                    req.setMethod("PUT");
                                    return req;
                                })
                                .header("X-USER-ID", testUserId))
                .andExpect(status().isNoContent());

        // then: 덮어쓰기 확인
        RecipeTagDocument updated = recipeTagRepository.findById(recipeId)
                .orElseThrow();
        assertThat(updated.getTags()).containsExactlyInAnyOrder("찌개", "매운맛");
    }
}
