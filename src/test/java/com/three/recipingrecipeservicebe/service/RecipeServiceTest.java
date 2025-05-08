package com.three.recipingrecipeservicebe.service;

import com.three.recipingrecipeservicebe.global.exception.custom.ForbiddenException;
import com.three.recipingrecipeservicebe.hashtag.entity.RecipeTagDocument;
import com.three.recipingrecipeservicebe.hashtag.repository.RecipeTagRepository;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeCreatedResponseDto;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeDetailResponseDto;
import com.three.recipingrecipeservicebe.recipe.dto.RecipeRequestDto;
import com.three.recipingrecipeservicebe.recipe.entity.Recipe;
import com.three.recipingrecipeservicebe.recipe.mapper.RecipeRepository;
import com.three.recipingrecipeservicebe.recipe.service.RecipeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RecipeServiceTest {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeTagRepository recipeTagRepository;

    @Test
    @DisplayName("CREATE 이미지가 있는 레시피 생성")
    void createRecipeWithImage() {
        // given
        RecipeRequestDto dto = RecipeRequestDto.builder()
                .title("오므라이스")
                .content("계란과 밥을 볶아서 만든 요리")
                .cookingTime(15)
                .difficulty("easy")
                .category("한식")
                .occasion("점심")
                .method("볶음")
                .ingredient("계란, 밥, 케찹")
                .build();

        Long userId = 1L;

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "uuid.jpg",
                "image/jpeg",
                "fake-image-content".getBytes()
        );

        // when
        RecipeCreatedResponseDto response = recipeService.createRecipe(dto, userId, file);

        // then
        assertThat(response.getImageUrl()).contains(".s3.amazonaws.com");
        assertThat(response.getTitle()).isEqualTo("오므라이스");
        assertThat(response.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("CREATE 이미지가 없는 레시피 생성")
    void createRecipeWithoutImage() {
        // given
        RecipeRequestDto dto = RecipeRequestDto.builder()
                .title("된장찌개")
                .content("된장, 두부, 채소 등을 넣고 끓인다.")
                .cookingTime(20)
                .difficulty("medium")
                .category("한식")
                .occasion("저녁")
                .method("끓이기")
                .ingredient("된장, 두부, 애호박")
                .build();

        Long userId = 1L;

        // when
        RecipeCreatedResponseDto response = recipeService.createRecipe(dto, userId, null);

        // then
        assertThat(response.getTitle()).isEqualTo("된장찌개");
        assertThat(response.getImageUrl()).isNull(); // 이미지 없는 경우
    }

    @Test
    @DisplayName("UPDATE 이미지 삭제만 요청한 경우")
    void updateRecipe_removeImageOnly() {
        // given
        Recipe saved = recipeRepository.save(Recipe.builder()
                .title("된장찌개")
                .content("설명")
                .userId(1L)
                .imageUrl("https://s3.amazonaws.com/image/old.jpg")
                .build());

        RecipeRequestDto dto = RecipeRequestDto.builder()
                .title("된장찌개")
                .content("설명")
                .shouldRemoveImage(true)
                .build();

        // when
        recipeService.updateRecipe(saved.getId(), dto, 1L, null);

        // then
        Recipe updated = recipeRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getImageUrl()).isNull();
    }

    @Test
    @DisplayName("UPDATE 이미지 업로드만 요청한 경우")
    void updateRecipe_uploadImageOnly() {
        // given
        Recipe saved = recipeRepository.save(Recipe.builder()
                .title("김치찌개")
                .content("설명")
                .userId(1L)
                .build());

        RecipeRequestDto dto = RecipeRequestDto.builder()
                .title("김치찌개")
                .content("설명")
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "file", "new.jpg", "image/jpeg", "new-image".getBytes()
        );

        // when
        recipeService.updateRecipe(saved.getId(), dto, 1L, file);

        // then
        Recipe updated = recipeRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getImageUrl()).contains(".s3.amazonaws.com");
        assertThat(updated.getKeyName()).isNotEqualTo("image/old.jpg");
    }

    @Test
    @DisplayName("UPDATE 이미지 삭제와 업로드 모두 요청한 경우")
    void updateRecipe_removeAndUploadImage() {
        // given
        Recipe saved = recipeRepository.save(Recipe.builder()
                .title("카레라이스")
                .content("설명")
                .userId(1L)
                .imageUrl("https://s3.amazonaws.com/image/old.jpg")
                .build());

        RecipeRequestDto dto = RecipeRequestDto.builder()
                .title("카레라이스")
                .content("설명")
                .shouldRemoveImage(true)
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "file", "new.jpg", "image/jpeg", "new-image".getBytes()
        );

        // when
        recipeService.updateRecipe(saved.getId(), dto, 1L, file);

        // then
        Recipe updated = recipeRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getImageUrl()).contains(".s3.amazonaws.com");
        assertThat(updated.getKeyName()).isNotEqualTo("image/old.jpg");
    }

    @Test
    @DisplayName("UPDATE 이미지 삭제/업로드 모두 없는 경우")
    void updateRecipe_noImageChange() {
        // given
        Recipe saved = recipeRepository.save(Recipe.builder()
                .title("오므라이스")
                .content("기존 설명")
                .userId(1L)
                .keyName("image/unchanged.jpg")
                .imageUrl("https://s3.amazonaws.com/image/unchanged.jpg")
                .build());

        RecipeRequestDto dto = RecipeRequestDto.builder()
                .title("오므라이스")
                .content("수정된 설명")
                .build();

        // when
        recipeService.updateRecipe(saved.getId(), dto, 1L, null);

        // then
        Recipe updated = recipeRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getContent()).isEqualTo("수정된 설명");
        assertThat(updated.getKeyName()).isEqualTo("image/unchanged.jpg");
    }

    @Test
    @DisplayName("UPDATE 권한 없는 사용자 요청 시 예외 발생")
    void updateRecipe_unauthorizedUser() {
        // given
        Recipe saved = recipeRepository.save(Recipe.builder()
                .title("미역국")
                .content("설명")
                .userId(1L)
                .build());

        RecipeRequestDto dto = RecipeRequestDto.builder()
                .title("미역국")
                .content("설명")
                .build();

        // when & then
        assertThatThrownBy(() ->
                recipeService.updateRecipe(saved.getId(), dto, 2L, null)
        ).isInstanceOf(ForbiddenException.class);
    }

    @Test
    @DisplayName("CREATE 레시피 ID로 상세 정보, 태그 조회")
    void getRecipeById_withTags() {
        // given
        Recipe savedRecipe = recipeRepository.save(Recipe.builder()
                .title("김치볶음밥")
                .content("김치와 밥을 볶는다.")
                .userId(1L)
                .build());

        recipeTagRepository.save(RecipeTagDocument.builder()
                .recipeId(savedRecipe.getId())
                .tags(List.of("김치", "볶음"))
                .build());

        // when
        RecipeDetailResponseDto dto = recipeService.getRecipeById(savedRecipe.getId());

        // then
        assertThat(dto.getTitle()).isEqualTo("김치볶음밥");
        assertThat(dto.getTags()).containsExactlyInAnyOrder("김치", "볶음");
    }
}
