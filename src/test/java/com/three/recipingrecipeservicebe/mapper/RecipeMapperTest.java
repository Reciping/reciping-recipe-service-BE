package com.three.recipingrecipeservicebe.mapper;

import com.three.recipingrecipeservicebe.recipe.dto.RecipeRequestDto;
import com.three.recipingrecipeservicebe.recipe.entity.Recipe;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RecipeMapperTest {

    @Test
    @DisplayName("UPDATE Entity - DTO 매퍼 사용 수정 테스트")
    void updateFrom() {
        // Given
        RecipeRequestDto dto = RecipeRequestDto.builder()
                .title("새 제목")
                .cookingTime(30)
                .build();

        Recipe entity = Recipe.builder()
                .title("기존 제목")
                .content("기존 내용")
                .cookingTime(10)
                .build();

        // When
        entity.updateFromDto(dto);

        // Then
        assertThat(entity.getTitle()).isEqualTo("새 제목");
        assertThat(entity.getContent()).isEqualTo("기존 내용"); // null인 필드는 기존 값 유지
        assertThat(entity.getCookingTime()).isEqualTo(30);
    }
}
