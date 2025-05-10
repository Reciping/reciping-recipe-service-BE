package com.three.recipingrecipeservicebe.service;

import com.three.recipingrecipeservicebe.bookmark.entity.BookmarkCount;
import com.three.recipingrecipeservicebe.bookmark.entity.RecipeBookmarkDocument;
import com.three.recipingrecipeservicebe.bookmark.service.BookmarkAggregateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(BookmarkAggregateService.class)
@TestPropertySource(properties = {
        "spring.data.mongodb.uri=mongodb://localhost:27017/recipe_bookmark"
})
class BookmarkAggregateServiceTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BookmarkAggregateService bookmarkAggregateService;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection("recipe_bookmarks");
        mongoTemplate.dropCollection("bookmark_stats");
    }

    @Test
    @DisplayName("레시피 즐겨찾기 수를 올바르게 집계하여 bookmark_stats에 저장한다")
    void aggregateRecipeBookmarkCount_savesCorrectCounts() {
        // given: recipe_bookmarks 컬렉션에 다양한 레시피 ID로 즐겨찾기 문서 삽입
        mongoTemplate.insertAll(List.of(
                RecipeBookmarkDocument.builder().recipeId(100L).userId(1L).build(),
                RecipeBookmarkDocument.builder().recipeId(100L).userId(2L).build(),
                RecipeBookmarkDocument.builder().recipeId(100L).userId(3L).build(),
                RecipeBookmarkDocument.builder().recipeId(200L).userId(4L).build(),
                RecipeBookmarkDocument.builder().recipeId(300L).userId(5L).build(),
                RecipeBookmarkDocument.builder().recipeId(300L).userId(6L).build()
        ));

        // when: 집계 메서드 호출
        bookmarkAggregateService.aggregateRecipeBookmarkCount();

        // then: bookmark_stats 컬렉션 확인
        List<BookmarkCount> result = mongoTemplate.findAll(BookmarkCount.class, "bookmark_stats");

        assertThat(result).hasSize(3);
        assertThat(result).anyMatch(b -> b.getRecipeId().equals(100L) && b.getCount() == 3);
        assertThat(result).anyMatch(b -> b.getRecipeId().equals(200L) && b.getCount() == 1);
        assertThat(result).anyMatch(b -> b.getRecipeId().equals(300L) && b.getCount() == 2);
    }
}
