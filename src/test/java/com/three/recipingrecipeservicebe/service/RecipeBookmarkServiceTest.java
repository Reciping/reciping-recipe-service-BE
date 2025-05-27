package com.three.recipingrecipeservicebe.service;

import com.three.recipingrecipeservicebe.bookmark.dto.BookmarkRequestDto;
import com.three.recipingrecipeservicebe.bookmark.entity.RecipeBookmarkDocument;
import com.three.recipingrecipeservicebe.bookmark.mapper.BookmarkMapperImpl;
import com.three.recipingrecipeservicebe.bookmark.repository.RecipeBookmarkRepository;
import com.three.recipingrecipeservicebe.bookmark.service.RecipeBookmarkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

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
    @DisplayName("31번째 즐겨찾기 등록 시 예외 발생")
    void toggleBookmark_throwsException_whenExceedsLimit() {
        // given
        Long userId = 1L;
        // 기존 30개 데이터 세팅
        for (int i = 0; i < 30; i++) {
            recipeBookmarkRepository.save(
                    RecipeBookmarkDocument.builder()
                            .userId(userId)
                            .recipeId((long) i)
                            .build()
            );
        }

        BookmarkRequestDto request = BookmarkRequestDto.builder()
                .userId(userId)
                .recipeId(9999L)
                .build();

        // when & then
        assertThatThrownBy(() -> recipeBookmarkService.toggleBookmark(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("즐겨찾기는 최대 30개까지만 가능합니다.");
    }
}
