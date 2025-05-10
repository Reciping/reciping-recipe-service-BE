package com.three.recipingrecipeservicebe.service;

import com.three.recipingrecipeservicebe.bookmark.entity.RecipeBookmarkDocument;
import com.three.recipingrecipeservicebe.bookmark.mapper.BookmarkMapper;
import com.three.recipingrecipeservicebe.bookmark.mapper.BookmarkMapperImpl;
import com.three.recipingrecipeservicebe.bookmark.repository.RecipeBookmarkRepository;
import com.three.recipingrecipeservicebe.bookmark.service.RecipeBookmarkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest
@Import({RecipeBookmarkService.class, BookmarkMapperImpl.class})
@TestPropertySource(properties = {
        "spring.data.mongodb.uri=mongodb://localhost:27017/recipe_bookmark"
})
public class RecipeBookmarkServiceTest {

    @Autowired
    private RecipeBookmarkRepository recipeBookmarkRepository;

    @Autowired
    private RecipeBookmarkService recipeBookmarkService;

    @BeforeEach
    void setUp() {
        recipeBookmarkRepository.deleteAll();
    }

    @Test
    @DisplayName("즐겨찾기가 30개 초과되면 예외를 던진다")
    void toggleBookmark_exceedLimit_shouldThrowException() {
        Long userId = 1L;

        // 30개 즐겨찾기 저장
        IntStream.range(0, 30).forEach(i ->
                recipeBookmarkRepository.save(
                        RecipeBookmarkDocument.builder()
                                .userId(userId)
                                .recipeId(1000L + i)
                                .build()
                )
        );

        // 31번째 즐겨찾기 시도 → 예외 발생
        assertThatThrownBy(() -> recipeBookmarkService.toggleBookmark( userId, 9999L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("즐겨찾기는 최대 30개까지만 가능합니다.");
    }
}
