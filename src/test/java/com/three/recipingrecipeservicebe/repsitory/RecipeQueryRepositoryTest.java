package com.three.recipingrecipeservicebe.repsitory;

import com.three.recipingrecipeservicebe.recipe.dto.RecipeSearchConditionRequestDto;
import com.three.recipingrecipeservicebe.recipe.entity.DishType;
import com.three.recipingrecipeservicebe.recipe.entity.Recipe;
import com.three.recipingrecipeservicebe.recipe.entity.SituationType;
import com.three.recipingrecipeservicebe.recipe.repository.RecipeQueryRepository;
import com.three.recipingrecipeservicebe.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
class RecipeQueryRepositoryTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeQueryRepository recipeQueryRepositoryImpl;

    @BeforeEach
    void setup() {
        recipeRepository.save(Recipe.builder().userId(1L).title("A").dishType(DishType.BANCHAN).situationType(SituationType.DAILY).build());
        recipeRepository.save(Recipe.builder().userId(2L).title("B").dishType(DishType.SOUP).situationType(SituationType.HOLIDAY).build());
        recipeRepository.save(Recipe.builder().userId(3L).title("C").dishType(DishType.BANCHAN).situationType(SituationType.GUEST).build());
    }

    @Test
    @DisplayName("READ DishType이 BANCHAN인 레시피만 조회")
    void shouldFilterByDishType() {
        RecipeSearchConditionRequestDto condition = RecipeSearchConditionRequestDto.builder()
                .dishType(DishType.BANCHAN)
                .build();

        Page<Recipe> result = recipeQueryRepositoryImpl.searchByCondition(condition, PageRequest.of(0, 10));

        assertThat(result.getContent())
                .extracting(Recipe::getTitle)
                .containsExactlyInAnyOrder("A", "C");
    }

    @Test
    @DisplayName("READ 전체 검색 - 조건 없이 모든 레시피 반환")
    void shouldReturnAllRecipesWhenNoFilter() {
        // 모든 필드를 null로 설정
        RecipeSearchConditionRequestDto condition = RecipeSearchConditionRequestDto.builder().build();

        Page<Recipe> result = recipeQueryRepositoryImpl.searchByCondition(condition, PageRequest.of(0, 10));

        assertThat(result.getContent())
                .extracting(Recipe::getTitle)
                .containsExactlyInAnyOrder("A", "B", "C");
    }

    @Test
    @DisplayName("READ DishType = BANCHAN AND Situation = DAILY 조건 검색")
    void shouldFilterByMultipleConditions() {
        RecipeSearchConditionRequestDto condition = RecipeSearchConditionRequestDto.builder()
                .dishType(DishType.BANCHAN)
                .situationType(SituationType.DAILY)
                .build();

        Page<Recipe> result = recipeQueryRepositoryImpl.searchByCondition(condition, PageRequest.of(0, 10));

        assertThat(result.getContent())
                .extracting(Recipe::getTitle)
                .containsExactly("A"); // A만 해당
    }
}
