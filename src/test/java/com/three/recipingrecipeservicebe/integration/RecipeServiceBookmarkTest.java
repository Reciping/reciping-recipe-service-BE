package com.three.recipingrecipeservicebe.integration;

import com.three.recipingrecipeservicebe.bookmark.entity.RecipeBookmarkDocument;
import com.three.recipingrecipeservicebe.bookmark.repository.RecipeBookmarkRepository;
import com.three.recipingrecipeservicebe.recipe.dto.MyRecipeSummaryResponseDto;
import com.three.recipingrecipeservicebe.recipe.entity.Recipe;
import com.three.recipingrecipeservicebe.recipe.repository.RecipeRepository;
import com.three.recipingrecipeservicebe.recipe.service.RecipeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(properties = {
        "spring.data.mongodb.uri=mongodb://localhost:27017/recipe_bookmark"
})
class RecipeServiceBookmarkTest {

    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired private RecipeBookmarkRepository bookmarkRepository;
    @Autowired private RecipeService recipeService;

    @Test
    @DisplayName("READ userId로 북마크한 목록 출력")
    void shouldReturnOnlyBookmarkedRecipesForUser() {
        // given
        Long userId = 100L;

        Recipe r1 = recipeRepository.save(Recipe.builder().title("A").userId(1L).isDeleted(false).build());
        Recipe r2 = recipeRepository.save(Recipe.builder().title("B").userId(2L).isDeleted(false).build());
        Recipe r3 = recipeRepository.save(Recipe.builder().title("C").userId(3L).isDeleted(false).build());

        // 북마크: userId가 r2, r3를 즐겨찾기
        bookmarkRepository.save(RecipeBookmarkDocument.builder().userId(userId).recipeId(r2.getId()).build());
        bookmarkRepository.save(RecipeBookmarkDocument.builder().userId(userId).recipeId(r3.getId()).build());

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<MyRecipeSummaryResponseDto> result = recipeService.getBookmarkedRecipeList(userId, pageable);

        // then
        List<String> titles = result.getContent().stream()
                .map(MyRecipeSummaryResponseDto::getTitle)
                .toList();

        titles.forEach(System.out::println);

        assertThat(titles).containsExactly("B", "C");
    }
}
