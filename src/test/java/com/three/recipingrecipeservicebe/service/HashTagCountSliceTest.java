package com.three.recipingrecipeservicebe.service;

import com.three.recipingrecipeservicebe.hashtag.entity.HashTag;
import com.three.recipingrecipeservicebe.hashtag.entity.RecipeTagDocument;
import com.three.recipingrecipeservicebe.hashtag.repository.HashTagRepository;
import com.three.recipingrecipeservicebe.hashtag.repository.RecipeTagRepository;
import com.three.recipingrecipeservicebe.hashtag.service.HashTagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(HashTagService.class) // Service 로딩 명시
@TestPropertySource(properties = {
        "spring.data.mongodb.uri=mongodb://localhost:27017/recipe_hashtag"
})
class HashTagCountSliceTest {

    @Autowired
    private HashTagService hashTagService;

    @Autowired
    private RecipeTagRepository recipeTagRepository;

    @Autowired
    private HashTagRepository hashTagRepository;

    @BeforeEach
    void clean() {
        recipeTagRepository.deleteAll();
        hashTagRepository.deleteAll();
    }

    @Test
    @DisplayName("슬라이스 테스트 - 태그 카운트를 집계하고 해시태그 컬렉션에 저장")
    void updateTagCounts_sliceTest() {
        // given
        recipeTagRepository.save(new RecipeTagDocument(1L, List.of("한식", "간단")));
        recipeTagRepository.save(new RecipeTagDocument(2L, List.of("한식", "매운")));
        recipeTagRepository.save(new RecipeTagDocument(3L, List.of("매운")));

        // when
        hashTagService.updateTagCounts();

        // then
        List<HashTag> all = hashTagRepository.findAll();
        assertThat(all).hasSize(3);

        assertThat(all).anyMatch(tag -> tag.getName().equals("한식") && tag.getCount() == 2L);
        assertThat(all).anyMatch(tag -> tag.getName().equals("매운") && tag.getCount() == 2L);
        assertThat(all).anyMatch(tag -> tag.getName().equals("간단") && tag.getCount() == 1L);
    }
}
