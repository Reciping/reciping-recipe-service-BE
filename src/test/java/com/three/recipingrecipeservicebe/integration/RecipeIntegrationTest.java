package com.three.recipingrecipeservicebe.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.three.recipingrecipeservicebe.global.security.UserDetailsImpl;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
    @DisplayName("CREATE 이미지 없는 레시피 생성 (SecurityContext 기반)")
    void createRecipe() throws Exception {
        // given
        Long userId = 1L;
        String username = "testuser";
        UserDetailsImpl userDetails = new UserDetailsImpl(userId, username, "password");

        // SecurityContext에 유저 등록
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );

        RecipeRequestDto dto = RecipeRequestDto.builder()
                .title("김치볶음밥")
                .content("1. 김치 썰기\n2. 밥 볶기")
                .build();

        MockMultipartFile requestDto = new MockMultipartFile(
                "requestDto", "", "application/json", objectMapper.writeValueAsBytes(dto)
        );

        // when
        MvcResult result = mockMvc.perform(multipart("/api/v1/recipes")
                        .file(requestDto)
                        .with(csrf())) // CSRF 토큰 필요 시
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
    @DisplayName("UPDATE 레시피 수정 (multipart/form-data) - SecurityContext 기반")
    void updateRecipe() throws Exception {
        // given
        Long userId = 1L;
        String username = "testuser";
        UserDetailsImpl userDetails = new UserDetailsImpl(userId, username, "password");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );

        Recipe recipe = Recipe.builder()
                .title("김치볶음밥")
                .content("1. 김치 썰기\n2. 밥 볶기")
                .userId(userId)
                .build();
        recipeRepository.save(recipe);

        RecipeRequestDto requestDto = RecipeRequestDto.builder()
                .title("참치김치볶음밥")
                .content("1. 참치와 김치 볶기\n2. 밥 넣기")
                .build();

        MockMultipartFile requestDtoPart = new MockMultipartFile(
                "requestDto", "", "application/json", objectMapper.writeValueAsBytes(requestDto)
        );

        // when
        MvcResult result = mockMvc.perform(
                        multipart("/api/v1/recipes/" + recipe.getId())
                                .file(requestDtoPart)
                                .with(request -> {
                                    request.setMethod("PUT");
                                    return request;
                                })
                                .with(csrf())
                )
                .andExpect(status().isNoContent())
                .andReturn();

        System.out.println("응답 상태 코드: " + result.getResponse().getStatus());

        // then
        Recipe updated = recipeRepository.findById(recipe.getId()).orElseThrow();
        assertThat(updated.getTitle()).isEqualTo("참치김치볶음밥");
        assertThat(updated.getContent()).isEqualTo("1. 참치와 김치 볶기\n2. 밥 넣기");
        assertThat(updated.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("DELETE 레시피 삭제 - SecurityContext 기반")
    void deleteRecipe() throws Exception {
        // given
        Long userId = 1L;
        String username = "testuser";
        UserDetailsImpl userDetails = new UserDetailsImpl(userId, username, "password");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );

        Recipe recipe = Recipe.builder()
                .title("김치볶음밥")
                .content("1. 김치 썰기\n2. 밥 볶기")
                .userId(userId)
                .build();
        recipeRepository.save(recipe);

        // when
        MvcResult result = mockMvc.perform(
                        delete("/api/v1/recipes/" + recipe.getId())
                                .with(csrf())
                )
                .andExpect(status().isNoContent())
                .andReturn();

        System.out.println("응답 상태 코드: " + result.getResponse().getStatus());
        System.out.println("응답 본문: " + result.getResponse().getContentAsString());

        // then
        List<Recipe> allRecipes = recipeRepository.findAll();
        assertThat(allRecipes).isEmpty();
    }

    @Test
    @DisplayName("UPDATE 레시피 태그를 덮어쓰면 기존 태그가 새로운 태그로 바뀐다 (upsert)")
    void updateRecipe_overwriteTags() throws Exception {
        // given
        Long userId = 1L;
        String username = "testuser";
        UserDetailsImpl userDetails = new UserDetailsImpl(userId, username, "password");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );

        // 최초 생성
        RecipeRequestDto dto1 = RecipeRequestDto.builder()
                .userId(userId)
                .title("된장찌개")
                .content("된장에 채소 넣기")
                .tags(List.of("한식", "국물"))
                .build();

        MockMultipartFile requestDto1 = new MockMultipartFile(
                "requestDto", "", "application/json", objectMapper.writeValueAsBytes(dto1)
        );

        MvcResult result1 = mockMvc.perform(
                        multipart("/api/v1/recipes")
                                .file(requestDto1)
                                .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result1.getResponse().getContentAsString();
        Long recipeId = objectMapper.readTree(responseBody).get("data").get("id").asLong();

        // 기존 태그 확인
        RecipeTagDocument first = recipeTagRepository.findById(recipeId).orElseThrow();
        assertThat(first.getTags()).containsExactlyInAnyOrder("한식", "국물");

        // 수정 요청
        RecipeRequestDto dto2 = RecipeRequestDto.builder()
                .userId(userId)
                .title("된장찌개 수정")
                .content("변경된 설명")
                .tags(List.of("찌개", "매운맛"))
                .build();

        MockMultipartFile requestDto2 = new MockMultipartFile(
                "requestDto", "", "application/json", objectMapper.writeValueAsBytes(dto2)
        );

        mockMvc.perform(
                        multipart("/api/v1/recipes/" + recipeId)
                                .file(requestDto2)
                                .with(request -> {
                                    request.setMethod("PUT");
                                    return request;
                                })
                                .with(csrf()))
                .andExpect(status().isNoContent());

        // 수정된 태그 확인
        RecipeTagDocument updated = recipeTagRepository.findById(recipeId).orElseThrow();
        assertThat(updated.getTags()).containsExactlyInAnyOrder("찌개", "매운맛");
    }
}
